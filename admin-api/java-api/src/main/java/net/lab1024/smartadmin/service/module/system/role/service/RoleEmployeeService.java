package net.lab1024.smartadmin.service.module.system.role.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.constant.StringConst;
import net.lab1024.smartadmin.service.common.domain.PageResult;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.common.util.SmartPageUtil;
import net.lab1024.smartadmin.service.module.system.department.dao.DepartmentDao;
import net.lab1024.smartadmin.service.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.smartadmin.service.module.system.employee.EmployeeCacheService;
import net.lab1024.smartadmin.service.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.smartadmin.service.module.system.role.dao.RoleDao;
import net.lab1024.smartadmin.service.module.system.role.dao.RoleEmployeeDao;
import net.lab1024.smartadmin.service.module.system.role.domain.entity.RoleEmployeeEntity;
import net.lab1024.smartadmin.service.module.system.role.domain.entity.RoleEntity;
import net.lab1024.smartadmin.service.module.system.role.domain.form.RoleEmployeeQueryForm;
import net.lab1024.smartadmin.service.module.system.role.domain.form.RoleEmployeeUpdateForm;
import net.lab1024.smartadmin.service.module.system.role.domain.vo.RoleSelectedVO;
import net.lab1024.smartadmin.service.module.system.role.manager.RoleEmployeeManager;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色管理业务
 *
 * @author zzr
 * @date 2019/4/3
 */
@Service
public class RoleEmployeeService  {

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private RoleEmployeeManager roleEmployeeManager;
    @Autowired
    private EmployeeCacheService employeeCacheService;

    /**
     * 通过角色id，分页获取成员员工列表
     *
     * @param roleEmployeeQueryForm
     * @return
     */
    public ResponseDTO<PageResult<EmployeeVO>> queryEmployee(RoleEmployeeQueryForm roleEmployeeQueryForm) {
        Page page = SmartPageUtil.convert2PageQuery(roleEmployeeQueryForm);
        List<EmployeeVO> employeeDTOS = roleEmployeeDao.selectRoleEmployeeByName(page, roleEmployeeQueryForm);
        List<Long> departmentIdList = employeeDTOS.stream().filter(e -> e.getDepartmentId() != null).map(EmployeeVO::getDepartmentId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(departmentIdList)) {
            List<DepartmentEntity> departmentEntities = departmentDao.selectBatchIds(departmentIdList);
            Map<Long, String> departmentIdNameMap = departmentEntities.stream().collect(Collectors.toMap(DepartmentEntity::getId, DepartmentEntity::getName));
            employeeDTOS.forEach(e -> {
                e.setDepartmentName(departmentIdNameMap.getOrDefault(e.getDepartmentId(), StringConst.EMPTY_STR));
            });
        }
        PageResult<EmployeeVO> PageResult = SmartPageUtil.convert2PageResult(page, employeeDTOS, EmployeeVO.class);
        return ResponseDTO.ok(PageResult);
    }

    public List<EmployeeVO> getAllEmployeeByRoleId(Long roleId) {
        return roleEmployeeDao.selectEmployeeByRoleId(roleId);
    }

    /**
     * 移除员工角色
     *
     * @param employeeId
     * @param roleId
     * @return ResponseDTO<String>
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> removeRoleEmployee(Long employeeId, Long roleId) {
        if (null == employeeId || null == roleId) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR);
        }
        roleEmployeeDao.deleteByEmployeeIdRoleId(employeeId, roleId);
        employeeCacheService.clearCacheByEmployeeId(employeeId);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除角色的成员员工
     *
     * @param roleEmployeeUpdateForm
     * @return ResponseDTO<String>
     */
    public ResponseDTO<String> batchRemoveRoleEmployee(RoleEmployeeUpdateForm roleEmployeeUpdateForm) {
        roleEmployeeDao.batchDeleteEmployeeRole(roleEmployeeUpdateForm.getRoleId(), roleEmployeeUpdateForm.getEmployeeIdList());
        for (Long employeeId : roleEmployeeUpdateForm.getEmployeeIdList()) {
            employeeCacheService.clearCacheByEmployeeId(employeeId);
        }
        return ResponseDTO.ok();
    }

    /**
     * 批量添加角色的成员员工
     *
     * @param roleEmployeeUpdateForm
     * @return
     */
    public ResponseDTO<String> batchAddRoleEmployee(RoleEmployeeUpdateForm roleEmployeeUpdateForm) {
        Long roleId = roleEmployeeUpdateForm.getRoleId();
        List<Long> employeeIdList = roleEmployeeUpdateForm.getEmployeeIdList();
        // 保存新的角色员工
        List<RoleEmployeeEntity> roleEmployeeList = null;
        if (CollectionUtils.isNotEmpty(employeeIdList)) {
            roleEmployeeList = employeeIdList.stream()
                    .map(employeeId -> new RoleEmployeeEntity(roleId, employeeId))
                    .collect(Collectors.toList());
        }
        // 保存数据
        roleEmployeeManager.saveRoleEmployee(roleId, roleEmployeeList);
        for (Long employeeId : employeeIdList) {
            employeeCacheService.clearCacheByEmployeeId(employeeId);
        }
        return ResponseDTO.ok();
    }

    /**
     * 通过员工id获取员工角色
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<List<RoleSelectedVO>> getRolesByEmployeeId(Long employeeId) {
        List<Long> roleIds = roleEmployeeDao.selectRoleIdByEmployeeId(employeeId);
        List<RoleEntity> roleList = roleDao.selectList(null);
        List<RoleSelectedVO> result = SmartBeanUtil.copyList(roleList, RoleSelectedVO.class);
        result.stream().forEach(item -> item.setSelected(roleIds.contains(item.getId())));
        return ResponseDTO.ok(result);
    }

}
