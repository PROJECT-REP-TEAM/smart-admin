package net.lab1024.smartadmin.service.module.system.department;

import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.system.department.domain.dto.DepartmentCreateDTO;
import net.lab1024.smartadmin.service.module.system.department.domain.dto.DepartmentUpdateDTO;
import net.lab1024.smartadmin.service.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentEmployeeTreeVO;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentTreeVO;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentVO;
import net.lab1024.smartadmin.service.module.system.employee.EmployeeDao;
import net.lab1024.smartadmin.service.module.system.employee.domain.dto.EmployeeDTO;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门管理业务类
 *
 * @author listen
 * @date 2017/12/19 14:25
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentTreeService departmentTreeService;

    @Autowired
    protected DepartmentCacheManager departmentCacheManager;

    /**
     * 获取部门树形结构
     *
     * @return
     */
    public ResponseDTO<List<DepartmentTreeVO>> departmentTree() {
        List<DepartmentTreeVO> treeVOList = departmentCacheManager.departmentTreeCache();
        return ResponseDTO.ok(treeVOList);
    }

    /**
     * 自身以及所有下级的部门id列表
     *
     * @param departmentId
     * @return
     */
    public List<Long> selfAndChildrenIdList(Long departmentId) {
        return departmentCacheManager.departmentSelfAndChildrenIdCache(departmentId);
    }

    /**
     * 部门员工树
     *
     * @return
     */
    public ResponseDTO<List<DepartmentEmployeeTreeVO>> departmentEmployeeTree() {
        List<DepartmentTreeVO> treeVOList = departmentCacheManager.departmentTreeCache();
        if (CollectionUtils.isEmpty(treeVOList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        // 获取全部员工列表
        List<EmployeeDTO> employeeList = employeeDao.listAll();
        if (CollectionUtils.isEmpty(employeeList)) {
            return ResponseDTO.ok(SmartBeanUtil.copyList(treeVOList, DepartmentEmployeeTreeVO.class));
        }
        Map<Long, List<EmployeeDTO>> employeeMap = employeeList.stream().collect(Collectors.groupingBy(EmployeeDTO::getDepartmentId));
        //构建各部门的员工信息
        List<DepartmentEmployeeTreeVO> departmentEmployeeTreeVOList = this.buildTreeEmployee(treeVOList, employeeMap);

        return ResponseDTO.ok(departmentEmployeeTreeVOList);
    }

    /**
     * 递归构建每部门的员工信息
     *
     * @param treeVOList
     * @param employeeMap
     * @return
     */
    private List<DepartmentEmployeeTreeVO> buildTreeEmployee(List<DepartmentTreeVO> treeVOList, Map<Long, List<EmployeeDTO>> employeeMap) {
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
     * 获取所有部门和员工信息
     *
     * @param departmentName
     * @return
     */
    public ResponseDTO<List<DepartmentTreeVO>> departmentTreeByName(String departmentName) {
        // 获取全部部门列表
        List<DepartmentVO> departmentVOList = departmentDao.listAll();
        if (StringUtils.isNotBlank(departmentName)) {
            // 检索条件不为空的时候 过滤部门列表
            departmentVOList = this.filterDepartment(departmentVOList, departmentName);
        }
        List<DepartmentTreeVO> result = departmentTreeService.buildTree(departmentVOList);
        return ResponseDTO.ok(result);
    }

    /**
     * 过滤部门名称，获取过滤后的结果
     *
     * @author 开云
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
            if (item.getParentId() != DepartmentConst.DEFAULT_PARENT_ID && item.getParentId() != null) {
                result.addAll(getParentDepartment(departmentVOList, item.getParentId(), result));
            }
        }
        return result;
    }

    /**
     * 新增添加部门
     *
     * @param departmentCreateDTO
     * @return AjaxResult
     */

    public ResponseDTO<String> addDepartment(DepartmentCreateDTO departmentCreateDTO) {
        DepartmentEntity departmentEntity = SmartBeanUtil.copy(departmentCreateDTO, DepartmentEntity.class);
        departmentEntity.setSort(0L);
        DepartmentService departmentService = (DepartmentService) AopContext.currentProxy();
        departmentService.addDepartmentHandle(departmentEntity);
        departmentCacheManager.clearTreeCache();
        departmentCacheManager.clearSelfAndChildrenIdCache();
        return ResponseDTO.ok();
    }

    /**
     * 新增部门数据处理
     *
     * @param departmentEntity
     */
    @Transactional(rollbackFor = Exception.class)
    public void addDepartmentHandle(DepartmentEntity departmentEntity) {
        departmentDao.insert(departmentEntity);
        DepartmentEntity updateSortEntity = new DepartmentEntity();
        updateSortEntity.setId(departmentEntity.getId());
        updateSortEntity.setSort(departmentEntity.getId());
        departmentDao.updateById(updateSortEntity);
    }

    /**
     * 更新部门信息
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> updateDepartment(DepartmentUpdateDTO updateDTO) {
        if (updateDTO.getParentId() == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "父级部门id不能为空");
        }
        DepartmentEntity entity = departmentDao.selectById(updateDTO.getId());
        if (entity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        DepartmentEntity departmentEntity = SmartBeanUtil.copy(updateDTO, DepartmentEntity.class);
        departmentEntity.setSort(entity.getSort());
        departmentDao.updateById(departmentEntity);
        departmentCacheManager.clearTreeCache();
        return ResponseDTO.ok();
    }

    /**
     * 根据id删除部门
     * 1、需要判断当前部门是否有子部门,有子部门则不允许删除
     * 2、需要判断当前部门是否有员工，有员工则不能删除
     *
     * @param deptId
     * @return
     */
    public ResponseDTO<String> delDepartment(Long deptId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(deptId);
        if (null == departmentEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 是否有子级部门
        int subDepartmentNum = departmentDao.countSubDepartment(deptId);
        if (subDepartmentNum > 0) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "请先删除子级部门");
        }

        // 是否有未删除员工
        int employeeNum = employeeDao.countByDepartmentId(deptId, false);
        if (employeeNum > 0) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "请先删除部门员工");
        }
        departmentDao.deleteById(deptId);
        // 清除缓存
        departmentCacheManager.clearTreeCache();
        departmentCacheManager.clearSelfAndChildrenIdCache();
        return ResponseDTO.ok();
    }

    /**
     * 根据id获取部门信息
     *
     * @param departmentId
     * @return
     */
    public ResponseDTO<DepartmentVO> getDepartmentById(Long departmentId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        DepartmentVO departmentVO = SmartBeanUtil.copy(departmentEntity, DepartmentVO.class);
        return ResponseDTO.ok(departmentVO);
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
     * 上下移动
     *
     * @param departmentId
     * @param swapId
     * @return
     */
    public ResponseDTO<String> upOrDown(Long departmentId, Long swapId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        DepartmentEntity swapEntity = departmentDao.selectById(swapId);
        if (swapEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        DepartmentEntity updateEntity = new DepartmentEntity();
        updateEntity.setId(departmentId);
        updateEntity.setSort(swapEntity.getSort());

        DepartmentEntity swapUpdateEntity = new DepartmentEntity();
        swapUpdateEntity.setId(swapEntity.getId());
        swapUpdateEntity.setSort(departmentEntity.getSort());
        DepartmentService departmentService = (DepartmentService) AopContext.currentProxy();
        departmentService.upOrDownUpdate(updateEntity, swapEntity);
        //清除缓存
        departmentCacheManager.clearTreeCache();
        return ResponseDTO.ok();
    }

    /**
     * 上下移动数据库处理
     *
     * @param departmentEntity
     * @param swapEntity
     */
    @Transactional(rollbackFor = Exception.class)
    public void upOrDownUpdate(DepartmentEntity departmentEntity, DepartmentEntity swapEntity) {
        departmentDao.updateById(departmentEntity);
        departmentDao.updateById(swapEntity);
    }

    /**
     * 部门升级
     *
     * @param departmentId
     * @return
     */
    public ResponseDTO<String> upgrade(Long departmentId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        if (departmentEntity.getParentId() == null || departmentEntity.getParentId().equals(DepartmentConst.DEFAULT_PARENT_ID)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "此部门已经是根节点无法移动");
        }
        DepartmentEntity parentEntity = departmentDao.selectById(departmentEntity.getParentId());

        DepartmentEntity updateEntity = new DepartmentEntity();
        updateEntity.setId(departmentId);
        updateEntity.setParentId(parentEntity.getParentId());
        departmentDao.updateById(updateEntity);
        //清除缓存
        departmentCacheManager.clearTreeCache();
        return ResponseDTO.ok();
    }

    /**
     * 部门降级
     *
     * @param departmentId
     * @param preId
     * @return
     */
    public ResponseDTO<String> downgrade(Long departmentId, Long preId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        DepartmentEntity preEntity = departmentDao.selectById(preId);
        if (preEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        DepartmentEntity updateEntity = new DepartmentEntity();
        updateEntity.setId(departmentId);
        updateEntity.setParentId(preEntity.getId());
        departmentDao.updateById(updateEntity);
        //清除缓存
        departmentCacheManager.clearTreeCache();
        return ResponseDTO.ok();
    }


    /**
     * 获取校区列表 即第二级部门列表
     *
     * @return
     */
    public ResponseDTO<List<DepartmentVO>> querySchoolList() {
        ResponseDTO<List<DepartmentTreeVO>> res = departmentTree();
        if (!res.getOk()) {
            return ResponseDTO.error(res);
        }
        List<DepartmentTreeVO> data = res.getData();
        // 拿到第二级部门列表
        List<DepartmentVO> resList = Lists.newArrayList();
        for (DepartmentTreeVO tree : data) {
            List<DepartmentTreeVO> children = tree.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                resList.addAll(SmartBeanUtil.copyList(children, DepartmentVO.class));
            }
        }
        return ResponseDTO.ok(resList);
    }


}
