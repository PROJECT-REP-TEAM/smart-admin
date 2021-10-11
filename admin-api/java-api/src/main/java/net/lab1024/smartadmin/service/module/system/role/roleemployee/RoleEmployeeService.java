package net.lab1024.smartadmin.service.module.system.role.roleemployee;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.domain.PageResultDTO;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.common.util.SmartPageUtil;
import net.lab1024.smartadmin.service.module.system.department.DepartmentDao;
import net.lab1024.smartadmin.service.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.smartadmin.service.module.system.employee.EmployeeCacheManager;
import net.lab1024.smartadmin.service.module.system.employee.domain.dto.EmployeeDTO;
import net.lab1024.smartadmin.service.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.smartadmin.service.module.system.role.basic.RoleDao;
import net.lab1024.smartadmin.service.module.system.role.basic.domain.dto.RoleQueryForm;
import net.lab1024.smartadmin.service.module.system.role.basic.domain.entity.RoleEntity;
import net.lab1024.smartadmin.service.module.system.role.basic.domain.vo.RoleSelectedVO;
import net.lab1024.smartadmin.service.module.system.role.roleemployee.domain.RoleEmployeeBatchDTO;
import net.lab1024.smartadmin.service.module.system.role.roleemployee.domain.RoleEmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色管理业务
 *
 * @author 1024lab
 * @date 2019/4/3
 */
@Service
public class RoleEmployeeService {

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private RoleEmployeeManager roleEmployeeManager;

    @Autowired
    protected EmployeeCacheManager employeeCacheManager;

    /**
     * 通过角色id，分页获取成员员工列表
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResultDTO<EmployeeVO>> listEmployeeByName(RoleQueryForm queryDTO) {
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<EmployeeDTO> employeeDTOS = roleEmployeeDao.selectEmployeeByNamePage(page, queryDTO);
        employeeDTOS.stream().filter(e -> e.getDepartmentId() != null).forEach(employeeDTO -> {
            DepartmentEntity departmentEntity = departmentDao.selectById(employeeDTO.getDepartmentId());
            employeeDTO.setDepartmentName(departmentEntity.getName());
        });
        PageResultDTO<EmployeeVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, employeeDTOS, EmployeeVO.class);
        return ResponseDTO.ok(pageResultDTO);
    }

    public ResponseDTO<List<EmployeeVO>> getAllEmployeeByRoleId(Long roleId) {
        List<EmployeeDTO> employeeDTOS = roleEmployeeDao.selectEmployeeByRoleId(roleId);
        List<EmployeeVO> list = SmartBeanUtil.copyList(employeeDTOS, EmployeeVO.class);
        return ResponseDTO.ok(list);
    }

    /**
     * 移除员工角色
     *
     * @param employeeId
     * @param roleId
     * @return RespDTO<String>
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> removeEmployeeRole(Long employeeId, Long roleId) {
        if (null == employeeId || null == roleId) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR);
        }
        roleEmployeeDao.deleteByEmployeeIdRoleId(employeeId, roleId);
        employeeCacheManager.clearCacheByEmployeeId(employeeId);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除角色的成员员工
     *
     * @param removeDTO
     * @return RespDTO<String>
     */
    public ResponseDTO<String> batchRemoveEmployeeRole(RoleEmployeeBatchDTO removeDTO) {
        roleEmployeeDao.batchDeleteEmployeeRole(removeDTO.getRoleId(), removeDTO.getEmployeeIdList());
        for (Long employeeId : removeDTO.getEmployeeIdList()) {
            employeeCacheManager.clearCacheByEmployeeId(employeeId);
        }
        return ResponseDTO.ok();
    }

    /**
     * 批量添加角色的成员员工
     *
     * @param addDTO
     * @return
     */
    public ResponseDTO<String> batchAddRoleEmployee(RoleEmployeeBatchDTO addDTO) {
        Long roleId = addDTO.getRoleId();
        List<Long> employeeIdList = addDTO.getEmployeeIdList();
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
            employeeCacheManager.clearCacheByEmployeeId(employeeId);
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
