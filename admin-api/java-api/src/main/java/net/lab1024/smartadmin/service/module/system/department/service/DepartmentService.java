package net.lab1024.smartadmin.service.module.system.department.service;

import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.module.system.department.dao.DepartmentDao;
import net.lab1024.smartadmin.service.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.smartadmin.service.module.system.department.domain.form.DepartmentAddForm;
import net.lab1024.smartadmin.service.module.system.department.domain.form.DepartmentUpdateForm;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentEmployeeTreeVO;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentTreeVO;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentVO;
import net.lab1024.smartadmin.service.module.system.employee.EmployeeDao;
import net.lab1024.smartadmin.service.module.system.employee.domain.vo.EmployeeVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门管理
 *
 * @author zhuoda
 */
@Service
public class DepartmentService {

    static final long DEFAULT_PARENT_ID = 0L;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentCacheService departmentCacheService;


    /**
     * 获取部门树形结构
     *
     * @return
     */
    public ResponseDTO<List<DepartmentTreeVO>> departmentTree() {
        List<DepartmentTreeVO> treeVOList = departmentCacheService.departmentTreeCache();
        return ResponseDTO.ok(treeVOList);
    }

    /**
     * 自身以及所有下级的部门id列表
     *
     * @param departmentId
     * @return
     */
    public List<Long> selfAndChildrenIdList(Long departmentId) {
        return departmentCacheService.departmentSelfChildrenCache(departmentId);
    }


    /**
     * 递归构建每部门的员工信息
     *
     * @param treeVOList
     * @param employeeMap
     * @return
     */
    private List<DepartmentEmployeeTreeVO> buildTreeEmployee(List<DepartmentTreeVO> treeVOList, Map<Long, List<EmployeeVO>> employeeMap) {
        List<DepartmentEmployeeTreeVO> departmentEmployeeTreeVOList = Lists.newArrayList();
        for (DepartmentTreeVO departmentTreeVO : treeVOList) {
            DepartmentEmployeeTreeVO departmentEmployeeTreeVO = SmartBeanUtil.copy(departmentTreeVO, DepartmentEmployeeTreeVO.class);
            departmentEmployeeTreeVO.setEmployees(employeeMap.getOrDefault(departmentEmployeeTreeVO.getId(), Lists.newArrayList()));
            List<DepartmentTreeVO> children = departmentTreeVO.getChildren();
            if (CollectionUtils.isEmpty(children)) {
                continue;
            }
            List<DepartmentEmployeeTreeVO> childrenList = this.buildTreeEmployee(children, employeeMap);
            departmentEmployeeTreeVO.setChildren(childrenList);
            departmentEmployeeTreeVOList.add(departmentEmployeeTreeVO);
        }
        return departmentEmployeeTreeVOList;
    }


    /**
     * 过滤部门名称，获取过滤后的结果
     *
     * @author lidoudou
     * @date 2019/4/28 20:17
     */
    private List<DepartmentVO> filterDepartment(List<DepartmentVO> departmentVOList, String departmentName) {
        Map<Long, DepartmentVO> departmentMap = new HashMap<>(departmentVOList.size());
        departmentVOList.forEach(item -> {
            if (!item.getName().contains(departmentName)) {
                return;
            }
            // 当前部门包含关键字
            departmentMap.put(item.getId(), item);
            Long parentId = item.getParentId();
            if (null != parentId) {
                List<DepartmentVO> filterResult = new ArrayList<>();
                getParentDepartment(departmentVOList, parentId, filterResult);
                for (DepartmentVO dto : filterResult) {
                    if (!departmentMap.containsKey(dto.getId())) {
                        departmentMap.put(dto.getId(), dto);
                    }
                }
            }
        });
        return new ArrayList<>(departmentMap.values());
    }

    /**
     * 递归获取部门的所有上级元素
     *
     * @param departmentVOList
     * @param parentId
     * @param result
     * @return
     */
    private List<DepartmentVO> getParentDepartment(List<DepartmentVO> departmentVOList, Long parentId, List<DepartmentVO> result) {
        List<DepartmentVO> deptList = departmentVOList.stream().filter(e -> e.getId().equals(parentId)).collect(Collectors.toList());
        for (DepartmentVO item : deptList) {
            result.add(item);
            if (item.getParentId() != DEFAULT_PARENT_ID && item.getParentId() != null) {
                result.addAll(getParentDepartment(departmentVOList, item.getParentId(), result));
            }
        }
        return result;
    }

    /**
     * 新增添加部门
     *
     * @param departmentAddForm
     * @return AjaxResult
     */

    public ResponseDTO<String> addDepartment(DepartmentAddForm departmentAddForm) {
        DepartmentEntity departmentEntity = SmartBeanUtil.copy(departmentAddForm, DepartmentEntity.class);
        DepartmentService departmentService = (DepartmentService) AopContext.currentProxy();
        departmentDao.insert(departmentEntity);
        this.clearCache();
        return ResponseDTO.ok();
    }


    /**
     * 更新部门信息
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> updateDepartment(DepartmentUpdateForm updateDTO) {
        if (updateDTO.getParentId() == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "父级部门id不能为空");
        }
        DepartmentEntity entity = departmentDao.selectById(updateDTO.getId());
        if (entity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        DepartmentEntity departmentEntity = SmartBeanUtil.copy(updateDTO, DepartmentEntity.class);
        departmentEntity.setSort(updateDTO.getSort());
        departmentDao.updateById(departmentEntity);
        this.clearCache();
        return ResponseDTO.ok();
    }

    /**
     * 根据id删除部门
     * 1、需要判断当前部门是否有子部门,有子部门则不允许删除
     * 2、需要判断当前部门是否有员工，有员工则不能删除
     *
     * @param departmentId
     * @return
     */
    public ResponseDTO<String> deleteDepartment(Long departmentId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (null == departmentEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 是否有子级部门
        int subDepartmentNum = departmentDao.countSubDepartment(departmentId);
        if (subDepartmentNum > 0) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "请先删除子级部门");
        }

        // 是否有未删除员工
        int employeeNum = employeeDao.countByDepartmentId(departmentId, false);
        if (employeeNum > 0) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "请先删除部门员工");
        }
        departmentDao.deleteById(departmentId);
        // 清除缓存
        this.clearCache();
        return ResponseDTO.ok();
    }


    /**
     * 获取所有部门
     *
     * @return
     */
    public ResponseDTO<List<DepartmentVO>> listAll() {
        List<DepartmentVO> departmentVOList = departmentDao.listAll();
        return ResponseDTO.ok(departmentVOList);
    }


    /**
     * 清除自身以及下级的id列表缓存
     */
    private void clearCache() {
        departmentCacheService.clearDepartmentCache();
        departmentCacheService.clearSelfAndChildrenIdCache();
        departmentCacheService.clearTreeCache();
        departmentCacheService.clearDepartmentSchoolCache();
        departmentCacheService.clearDepartmentRouteCache();
    }


}
