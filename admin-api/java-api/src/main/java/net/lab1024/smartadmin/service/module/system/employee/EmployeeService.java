package net.lab1024.smartadmin.service.module.system.employee;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.domain.PageResultDTO;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.common.util.SmartPageUtil;
import net.lab1024.smartadmin.service.module.system.department.DepartmentDao;
import net.lab1024.smartadmin.service.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.smartadmin.service.module.system.employee.domain.dto.*;
import net.lab1024.smartadmin.service.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.smartadmin.service.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginBO;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginInfoDTO;
import net.lab1024.smartadmin.service.module.system.menu.MenuEmployeeService;
import net.lab1024.smartadmin.service.module.system.role.roleemployee.RoleEmployeeDao;
import net.lab1024.smartadmin.service.module.system.role.roleemployee.domain.RoleEmployeeEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 员工管理
 *
 * @author 罗伊
 * @date 2021年01月21日上午11:54:52
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private MenuEmployeeService menuEmployeeService;

    @Autowired
    private EmployeeManager employeeManager;

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;

    @Autowired
    private EmployeeCacheManager employeeCacheManager;

    /**
     * 获取员工登录信息
     *
     * @param employeeId
     * @return
     */
    public EmployeeLoginInfoDTO getById(Long employeeId) {
        EmployeeEntity employeeEntity = employeeCacheManager.singleEmployeeCache(employeeId);
        List<Long> roleIdList = employeeCacheManager.singleEmployeeRoleCache(employeeId);
        if (employeeEntity != null) {
            Boolean isSuperman = menuEmployeeService.isSuperman(employeeId);
            EmployeeLoginInfoDTO loginDTO = SmartBeanUtil.copy(employeeEntity, EmployeeLoginInfoDTO.class);
            loginDTO.setEmployeeId(employeeId);
            loginDTO.setIsSuperMan(isSuperman);
            loginDTO.setRoleList(roleIdList);
            return loginDTO;
        }
        return null;
    }

    /**
     * 获取员工登录信息
     *
     * @param employeeId
     * @return
     */
    public EmployeeLoginBO getBoById(Long employeeId) {
        EmployeeEntity employeeEntity = employeeCacheManager.singleEmployeeCache(employeeId);
        List<Long> roleIdList = employeeCacheManager.singleEmployeeRoleCache(employeeId);
        if (employeeEntity != null) {
            Boolean isSuperman = menuEmployeeService.isSuperman(employeeId);
            EmployeeLoginBO loginDTO = SmartBeanUtil.copy(employeeEntity, EmployeeLoginBO.class);
            loginDTO.setEmployeeId(employeeId);
            loginDTO.setIsSuperMan(isSuperman);
            loginDTO.setRoleList(roleIdList);
            return loginDTO;
        }
        return null;
    }

    /**
     * 查询员工列表
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResultDTO<EmployeeVO>>  queryEmployeeList(EmployeeQueryForm queryDTO) {
        queryDTO.setDeletedFlag(false);
        Page pageParam = SmartPageUtil.convert2PageQuery(queryDTO);
        List<EmployeeVO> employeeList = employeeDao.queryEmployee(pageParam, queryDTO);
        if (CollectionUtils.isEmpty(employeeList)) {
            PageResultDTO<EmployeeVO> pageResultDTO = SmartPageUtil.convert2PageResult(pageParam, employeeList);
            return ResponseDTO.ok(pageResultDTO);
        }
        // 查询员工角色
        List<Long> employeeIdList = employeeList.stream().map(EmployeeVO::getId).collect(Collectors.toList());
        List<RoleEmployeeEntity> roleEmployeeEntityList = roleEmployeeDao.selectRoleIdByEmployeeIdList(employeeIdList);
        Map<Long, List<Long>> employeeRoleIdListMap = roleEmployeeEntityList.stream().collect(Collectors.groupingBy(RoleEmployeeEntity::getEmployeeId, Collectors.mapping(RoleEmployeeEntity::getRoleId, Collectors.toList())));
        // 写入角色ID
        employeeList.forEach(e -> {
            e.setRoleIdList(employeeRoleIdListMap.getOrDefault(e.getId(), Lists.newArrayList()));
        });
        PageResultDTO<EmployeeVO> pageResultDTO = SmartPageUtil.convert2PageResult(pageParam, employeeList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 新增员工
     *
     * @param addDTO
     * @return
     */
    public ResponseDTO<String> addEmployee(EmployeeAddDTO addDTO) {
        // 校验名称是否重复
        EmployeeDTO employeeDTO = employeeDao.getByLoginName(addDTO.getLoginName(), false, false);
        if (null != employeeDTO) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "员工名称重复");
        }
        // 校验电话是否存在
        employeeDTO = employeeDao.getByPhone(addDTO.getPhone(), false);
        if (null != employeeDTO) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "手机号已存在");
        }
        // 部门是否存在
        Long departmentId = addDTO.getDepartmentId();
        DepartmentEntity department = departmentDao.selectById(departmentId);
        if (department == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "部门不存在");
        }

        EmployeeEntity entity = SmartBeanUtil.copy(addDTO, EmployeeEntity.class);
        // 设置密码 默认密码
        entity.setLoginPwd(getEncryptPwd(null));

        // 保存数据
        employeeManager.saveEmployee(entity, addDTO.getRoleIdList());

        employeeCacheManager.clearCacheByDepartmentId(departmentId);

        return ResponseDTO.ok();
    }

    /**
     * 更新员工
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> updateEmployee(EmployeeUpdateDTO updateDTO) {
        Long employeeId = updateDTO.getId();
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (null == employeeEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        Long departmentId = updateDTO.getDepartmentId();
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        EmployeeDTO employeeDTO = employeeDao.getByLoginName(updateDTO.getLoginName(), false, false);
        if (null != employeeDTO && !Objects.equals(employeeDTO.getId(), employeeId)) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST);
        }
        employeeDTO = employeeDao.getByPhone(updateDTO.getLoginName(), false);
        if (null != employeeDTO && !Objects.equals(employeeDTO.getId(), employeeId)) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        // 不更新密码
        EmployeeEntity entity = SmartBeanUtil.copy(updateDTO, EmployeeEntity.class);
        entity.setLoginPwd(null);

        // 更新数据
        employeeManager.updateEmployee(entity, updateDTO.getRoleIdList());

        // 清除缓存
        employeeCacheManager.clearCacheByEmployeeId(employeeId);
        employeeCacheManager.clearCacheByDepartmentId(departmentId);

        return ResponseDTO.ok();
    }

    /**
     * 更新禁用状态
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<String> updateDisableFlag(Long employeeId) {
        if (null == employeeId) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (null == employeeEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        employeeDao.updateDisableFlag(employeeId, !employeeEntity.getDisabledFlag());

        employeeCacheManager.clearCacheByEmployeeId(employeeId);
        employeeCacheManager.clearCacheByDepartmentId(employeeEntity.getDepartmentId());
        return ResponseDTO.ok();
    }

    /**
     * 批量更新员工状态
     *
     * @param batchUpdateStatusDTO
     * @return
     */
    public ResponseDTO<String> batchUpdateDisableFlag(EmployeeDisabledUpdateDTO batchUpdateStatusDTO) {
        List<Long> employeeIdList = batchUpdateStatusDTO.getEmployeeIdList();
        List<EmployeeEntity> employeeEntityList = employeeDao.selectBatchIds(employeeIdList);
        if (employeeIdList.size() != employeeEntityList.size()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        employeeDao.batchUpdateDisableFlag(employeeIdList, batchUpdateStatusDTO.getDisabledFlag());

        // 清除缓存
        employeeEntityList.forEach(e -> {
            employeeCacheManager.clearCacheByEmployeeId(e.getId());
            employeeCacheManager.clearCacheByDepartmentId(e.getDepartmentId());
        });
        return ResponseDTO.ok();
    }

    /**
     * 批量删除员工
     *
     * @param employeeIdList 员工ID列表
     * @return
     */
    public ResponseDTO<String> batchUpdateDeleteFlag(List<Long> employeeIdList) {
        if (CollectionUtils.isEmpty(employeeIdList)) {
            return ResponseDTO.ok();
        }
        List<EmployeeEntity> employeeEntityList = employeeManager.listByIds(employeeIdList);
        if (CollectionUtils.isEmpty(employeeEntityList)) {
            return ResponseDTO.ok();
        }
        List<EmployeeEntity> deleteList = employeeIdList.stream().map(e -> {
            // 更新删除
            EmployeeEntity updateEmployee = new EmployeeEntity();
            updateEmployee.setId(e);
            updateEmployee.setDeletedFlag(true);
            return updateEmployee;
        }).collect(Collectors.toList());
        employeeManager.updateBatchById(deleteList);

        // 清除缓存
        employeeEntityList.forEach(e -> {
            employeeCacheManager.clearCacheByEmployeeId(e.getId());
            employeeCacheManager.clearCacheByDepartmentId(e.getDepartmentId());
        });
        return ResponseDTO.ok();
    }

    /**
     * 删除员工
     *
     * @param employeeId 员工ID
     * @return
     */
    public ResponseDTO<String> deleteEmployeeById(Long employeeId) {
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (null == employeeEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 更新删除
        EmployeeEntity updateEmployee = new EmployeeEntity();
        updateEmployee.setId(employeeId);
        updateEmployee.setDeletedFlag(true);
        employeeDao.updateById(updateEmployee);

        employeeCacheManager.clearCacheByEmployeeId(employeeId);
        employeeCacheManager.clearCacheByDepartmentId(employeeEntity.getDepartmentId());
        return ResponseDTO.ok();
    }

    /**
     * 批量更新部门
     *
     * @param updateDto
     * @return
     */
    public ResponseDTO<String> batchUpdateDepartment(EmployeeDepartmentUpdateDTO updateDto) {
        List<Long> employeeIdList = updateDto.getEmployeeIdList();
        List<EmployeeEntity> employeeEntityList = employeeDao.selectBatchIds(employeeIdList);
        if (employeeIdList.size() != employeeEntityList.size()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        List<EmployeeEntity> updateList = employeeIdList.stream().map(e -> {
            // 更新删除
            EmployeeEntity updateEmployee = new EmployeeEntity();
            updateEmployee.setId(e);
            updateEmployee.setDepartmentId(updateDto.getDepartmentId());
            return updateEmployee;
        }).collect(Collectors.toList());
        employeeManager.updateBatchById(updateList);

        // 清除缓存
        employeeEntityList.forEach(e -> {
            employeeCacheManager.clearCacheByEmployeeId(e.getId());
            employeeCacheManager.clearCacheByDepartmentId(e.getDepartmentId());
        });
        return ResponseDTO.ok();
    }

    /**
     * 更新用户角色
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> updateRole(EmployeeRoleUpdateDTO updateDTO) {
        Long employeeId = updateDTO.getEmployeeId();
        List<Long> roleIdList = updateDTO.getRoleIdList();

        // 保存新的角色信息
        List<RoleEmployeeEntity> roleEmployeeList = null;
        if (CollectionUtils.isNotEmpty(roleIdList)) {
            roleEmployeeList = roleIdList.stream()
                    .map(roleId -> new RoleEmployeeEntity(roleId, employeeId))
                    .collect(Collectors.toList());
        }

        // 更新数据
        employeeManager.updateEmployeeRole(employeeId, roleEmployeeList);
        return ResponseDTO.ok();
    }

    /**
     * 更新密码
     *
     * @param updatePwdDTO
     * @return
     */
    public ResponseDTO<String> updatePwd(EmployeeUpdatePwdDTO updatePwdDTO) {
        Long employeeId = updatePwdDTO.getEmployeeId();
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (employeeEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 校验原始密码
        String encryptPwd = getEncryptPwd(updatePwdDTO.getOldPwd());
        if (!Objects.equals(encryptPwd, employeeEntity.getLoginPwd())) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        // 新旧密码相同
        String newPwd = updatePwdDTO.getPwd();
        if (Objects.equals(updatePwdDTO.getOldPwd(), newPwd)) {
            return ResponseDTO.ok();
        }

        // 更新密码
        EmployeeEntity updateEntity = new EmployeeEntity();
        updateEntity.setId(employeeId);
        updateEntity.setLoginPwd(getEncryptPwd(newPwd));
        employeeDao.updateById(updateEntity);

        return ResponseDTO.ok();
    }

    /**
     * 获取某个部门的员工信息
     *
     * @param departmentId
     * @return
     */
    public ResponseDTO<List<EmployeeVO>> getEmployeeByDeptId(Long departmentId) {
        List<EmployeeEntity> employeeEntityList = employeeCacheManager.departmentEmployeeCache(departmentId);
        if (CollectionUtils.isEmpty(employeeEntityList)) {
            return ResponseDTO.ok(Collections.emptyList());
        }
        List<EmployeeVO> voList = SmartBeanUtil.copyList(employeeEntityList, EmployeeVO.class);
        return ResponseDTO.ok(voList);
    }


    /**
     * 重置密码
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<String> resetPassword(Integer employeeId) {
        String md5Password = getEncryptPwd(null);
        employeeDao.updatePassword(employeeId, md5Password);
        return ResponseDTO.ok();
    }

    /**
     * 获取 加密后 的密码
     *
     * @param pwd 密码为空 将使用原始密码
     * @return
     */
    public static String getEncryptPwd(String pwd) {
        pwd = StringUtils.isBlank(pwd) ? EmployeeConst.Password.DEFAULT : pwd;
        return DigestUtils.md5Hex(String.format(EmployeeConst.Password.SALT_FORMAT, pwd));
    }

    public static void main(String[] args) {
        System.out.println(getEncryptPwd("123456"));

    }

    /**
     * 查询全部员工
     *
     * @return
     */
    public ResponseDTO<List<EmployeeVO>> queryAllEmploy(Boolean disabledFlag) {
        EmployeeQueryForm queryDTO = new EmployeeQueryForm();
        queryDTO.setDeletedFlag(Boolean.FALSE);
        if (disabledFlag != null) {
            queryDTO.setDisabledFlag(disabledFlag);
        }
        List<EmployeeVO> employeeList = employeeDao.queryEmployee(queryDTO);
        return ResponseDTO.ok(employeeList);
    }
}
