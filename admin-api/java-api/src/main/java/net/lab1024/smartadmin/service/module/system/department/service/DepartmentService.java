package net.lab1024.smartadmin.service.module.system.department.service;

import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.module.system.department.dao.DepartmentDao;
import net.lab1024.smartadmin.service.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.smartadmin.service.module.system.department.domain.form.DepartmentAddForm;
import net.lab1024.smartadmin.service.module.system.department.domain.form.DepartmentUpdateForm;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentTreeVO;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentVO;
import net.lab1024.smartadmin.service.module.system.department.manager.DepartmentCacheManager;
import net.lab1024.smartadmin.service.module.system.employee.dao.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门管理
 *
 * @author zhuoda
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentCacheManager departmentCacheManager;


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
        return departmentCacheManager.departmentSelfChildrenCache(departmentId);
    }


    /**
     * 新增添加部门
     *
     * @param departmentAddForm
     * @return AjaxResult
     */

    public ResponseDTO<String> addDepartment(DepartmentAddForm departmentAddForm) {
        DepartmentEntity departmentEntity = SmartBeanUtil.copy(departmentAddForm, DepartmentEntity.class);
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
        DepartmentEntity entity = departmentDao.selectById(updateDTO.getDepartmentId());
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
        int employeeNum = employeeDao.countByDepartmentId(departmentId);
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
        departmentCacheManager.clearDepartmentCache();
        departmentCacheManager.clearSelfAndChildrenIdCache();
        departmentCacheManager.clearTreeCache();
        departmentCacheManager.clearDepartmentRouteCache();
    }


    public DepartmentEntity getDepartmentById(Long departmentId) {
        return departmentDao.selectById(departmentId);
    }
}
