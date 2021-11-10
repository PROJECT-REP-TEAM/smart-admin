package net.lab1024.smartadmin.service.module.system.employee.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.domain.PageResult;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.common.util.SmartPageUtil;
import net.lab1024.smartadmin.service.module.system.department.dao.DepartmentDao;
import net.lab1024.smartadmin.service.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentVO;
import net.lab1024.smartadmin.service.module.system.department.manager.DepartmentCacheManager;
import net.lab1024.smartadmin.service.module.system.employee.EmployeeDao;
import net.lab1024.smartadmin.service.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.smartadmin.service.module.system.employee.domain.form.*;
import net.lab1024.smartadmin.service.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.smartadmin.service.module.system.employee.manager.EmployeeCacheManager;
import net.lab1024.smartadmin.service.module.system.employee.manager.EmployeeManager;
import net.lab1024.smartadmin.service.module.system.login.domain.LoginUserDetail;
import net.lab1024.smartadmin.service.module.system.login.domain.RequestEmployee;
import net.lab1024.smartadmin.service.module.system.menu.service.MenuEmployeeService;
import net.lab1024.smartadmin.service.module.system.role.dao.RoleEmployeeDao;
import net.lab1024.smartadmin.service.module.system.role.domain.vo.RoleEmployeeVO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 员工管理
 *
 * @author yandanyang
 * @date 2021年01月21日上午11:54:52
 */
@Service
public class EmployeeService {

    private static final String DEFAULT_PASSWORD = "123456";

    private static final String PASSWORD_SALT_FORMAT = "smart_%_admin_$%^&*";

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
    @Autowired
    private DepartmentCacheManager departmentCacheManager;

    /**
     * 获取员工登录信息
     *
     * @param employeeId
     * @return
     */
    public RequestEmployee getById(Long employeeId) {
        EmployeeEntity employeeEntity = employeeCacheManager.singleEmployeeCache(employeeId);
        //获取员工角色缓存
        List<Long> roleIdList = employeeCacheManager.singleEmployeeRoleCache(employeeId);
        if (employeeEntity != null) {
            Boolean isSuperman = menuEmployeeService.isSuperman(employeeId);
            RequestEmployee loginDTO = SmartBeanUtil.copy(employeeEntity, RequestEmployee.class);
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
    public LoginUserDetail getBoById(Long employeeId) {
        EmployeeEntity employeeEntity = employeeCacheManager.singleEmployeeCache(employeeId);
        //获取员工角色缓存
        List<Long> roleIdList = employeeCacheManager.singleEmployeeRoleCache(employeeId);
        if (employeeEntity == null) {
            return null;
        }
        Boolean isSuperman = menuEmployeeService.isSuperman(employeeId);
        LoginUserDetail loginDTO = SmartBeanUtil.copy(employeeEntity, LoginUserDetail.class);
        loginDTO.setEmployeeId(employeeId);
        loginDTO.setIsSuperMan(isSuperman);
        loginDTO.setRoleList(roleIdList);
        return loginDTO;
    }

    /**
     * 查询员工列表
     *
     * @param employeeQueryForm
     * @return
     */
    public ResponseDTO<PageResult<EmployeeVO>> queryEmployee(EmployeeQueryForm employeeQueryForm) {
        Page pageParam = SmartPageUtil.convert2PageQuery(employeeQueryForm);
        List<EmployeeVO> employeeList = employeeDao.queryEmployee(pageParam, employeeQueryForm);
        if (CollectionUtils.isEmpty(employeeList)) {
            PageResult<EmployeeVO> PageResult = SmartPageUtil.convert2PageResult(pageParam, employeeList);
            return ResponseDTO.ok(PageResult);
        }

        List<Long> employeeIdList = employeeList.stream().map(EmployeeVO::getEmployeeId).collect(Collectors.toList());
        // 查询员工角色
        List<RoleEmployeeVO> roleEmployeeEntityList = roleEmployeeDao.selectRoleByEmployeeIdList(employeeIdList);
        Map<Long, List<Long>> employeeRoleIdListMap = roleEmployeeEntityList.stream().collect(Collectors.groupingBy(RoleEmployeeVO::getEmployeeId, Collectors.mapping(RoleEmployeeVO::getRoleId, Collectors.toList())));
        Map<Long, List<String>> employeeRoleNameListMap = roleEmployeeEntityList.stream().collect(Collectors.groupingBy(RoleEmployeeVO::getEmployeeId, Collectors.mapping(RoleEmployeeVO::getRoleName, Collectors.toList())));
        // 查询员工部门
        Map<Long, String> departmentNameMap = departmentCacheManager.departmentRouteCache();

        employeeList.forEach(e -> {
            e.setRoleIdList(employeeRoleIdListMap.getOrDefault(e.getEmployeeId(), Lists.newArrayList()));
            e.setRoleNameList(employeeRoleNameListMap.getOrDefault(e.getEmployeeId(), Lists.newArrayList()));
            e.setDepartmentName(departmentNameMap.getOrDefault(e.getDepartmentId(), ""));
        });
        PageResult<EmployeeVO> PageResult = SmartPageUtil.convert2PageResult(pageParam, employeeList);
        return ResponseDTO.ok(PageResult);
    }

    /**
     * 新增员工
     *
     * @param employeeAddForm
     * @return
     */
    public synchronized ResponseDTO<String> addEmployee(EmployeeAddForm employeeAddForm) {
        // 校验名称是否重复
        EmployeeEntity employeeEntity = employeeDao.getByLoginName(employeeAddForm.getLoginName(), false);
        if (null != employeeEntity) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "登录名重复");
        }
        // 校验姓名是否重复
        employeeEntity = employeeDao.getByActualName(employeeAddForm.getActualName(), false);
        if (null != employeeEntity) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "姓名重复");
        }
        // 校验电话是否存在
        employeeEntity = employeeDao.getByPhone(employeeAddForm.getPhone(), false);
        if (null != employeeEntity) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "手机号已存在");
        }
        // 部门是否存在
        Long departmentId = employeeAddForm.getDepartmentId();
        DepartmentEntity department = departmentDao.selectById(departmentId);
        if (department == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "部门不存在");
        }

        EmployeeEntity entity = SmartBeanUtil.copy(employeeAddForm, EmployeeEntity.class);
        // 设置密码 默认密码
        entity.setLoginPwd(getEncryptPwd(DEFAULT_PASSWORD));

        // 保存数据
        employeeManager.saveEmployee(entity, employeeAddForm.getRoleIdList());
        employeeCacheManager.clearCacheByDepartmentId(departmentId);

        return ResponseDTO.ok();
    }

    /**
     * 更新员工
     *
     * @param employeeUpdateForm
     * @return
     */
    public synchronized ResponseDTO<String> updateEmployee(EmployeeUpdateForm employeeUpdateForm) {

        Long employeeId = employeeUpdateForm.getEmployeeId();
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (null == employeeEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        // 部门是否存在
        Long departmentId = employeeUpdateForm.getDepartmentId();
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "部门不存在");
        }


        EmployeeEntity existEntity = employeeDao.getByLoginName(employeeUpdateForm.getLoginName(), false);
        if (null != existEntity && !Objects.equals(existEntity.getEmployeeId(), employeeId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "登录名重复");
        }

        existEntity = employeeDao.getByPhone(employeeUpdateForm.getPhone(), false);
        if (null != existEntity && !Objects.equals(existEntity.getEmployeeId(), employeeId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "手机号已存在");
        }

        existEntity = employeeDao.getByActualName(employeeUpdateForm.getActualName(), false);
        if (null != existEntity) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "姓名重复");
        }

        // 不更新密码
        EmployeeEntity entity = SmartBeanUtil.copy(employeeUpdateForm, EmployeeEntity.class);
        entity.setLoginPwd(null);

        // 更新数据
        employeeManager.updateEmployee(entity, employeeUpdateForm.getRoleIdList());

        // 清除缓存
        employeeCacheManager.clearCacheByEmployeeId(employeeId);
        employeeCacheManager.clearCacheByDepartmentId(departmentId);

        return ResponseDTO.ok();
    }

    /**
     * 更新禁用/启用状态
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
     * 批量更新部门
     *
     * @param batchUpdateDepartmentForm
     * @return
     */
    public ResponseDTO<String> batchUpdateDepartment(EmployeeBatchUpdateDepartmentForm batchUpdateDepartmentForm) {
        List<Long> employeeIdList = batchUpdateDepartmentForm.getEmployeeIdList();
        List<EmployeeEntity> employeeEntityList = employeeDao.selectBatchIds(employeeIdList);
        if (employeeIdList.size() != employeeEntityList.size()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 更新
        List<EmployeeEntity> updateList = employeeIdList.stream().map(e -> {
            EmployeeEntity updateEmployee = new EmployeeEntity();
            updateEmployee.setEmployeeId(e);
            updateEmployee.setDepartmentId(batchUpdateDepartmentForm.getDepartmentId());
            return updateEmployee;
        }).collect(Collectors.toList());
        employeeManager.updateBatchById(updateList);

        // 清除缓存
        employeeEntityList.forEach(e -> {
            employeeCacheManager.clearCacheByEmployeeId(e.getEmployeeId());
            employeeCacheManager.clearCacheByDepartmentId(e.getDepartmentId());
        });
        employeeCacheManager.clearCacheByDepartmentId(batchUpdateDepartmentForm.getDepartmentId());
        return ResponseDTO.ok();
    }


    /**
     * 更新密码
     *
     * @param updatePasswordForm
     * @return
     */
    public ResponseDTO<String> updatePassword(EmployeeUpdatePasswordForm updatePasswordForm) {
        Long employeeId = updatePasswordForm.getEmployeeId();
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (employeeEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 校验原始密码
        String encryptPwd = getEncryptPwd(updatePasswordForm.getOldPassword());
        if (!Objects.equals(encryptPwd, employeeEntity.getLoginPwd())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "原密码有误，请重新输入");
        }

        // 新旧密码相同
        String newPassword = updatePasswordForm.getNewPassword();
        if (Objects.equals(updatePasswordForm.getOldPassword(), newPassword)) {
            return ResponseDTO.ok();
        }

        // 更新密码
        EmployeeEntity updateEntity = new EmployeeEntity();
        updateEntity.setEmployeeId(employeeId);
        updateEntity.setLoginPwd(getEncryptPwd(newPassword));
        employeeDao.updateById(updateEntity);

        return ResponseDTO.ok();
    }

    /**
     * 获取某个部门的员工信息
     *
     * @param departmentId
     * @return
     */
    public ResponseDTO<List<EmployeeVO>> getAllEmployeeByDepartmentId(Long departmentId, Boolean disabledFlag) {
        List<EmployeeEntity> employeeEntityList = employeeCacheManager.departmentEmployeeCache(departmentId);
        if (disabledFlag != null) {
            employeeEntityList = employeeEntityList.stream().filter(e -> e.getDisabledFlag().equals(disabledFlag)).collect(Collectors.toList());
        }
        // 获取部门
        List<DepartmentVO> departmentList = departmentCacheManager.departmentCache();
        Optional<DepartmentVO> departmentVO = departmentList.stream().filter(e -> e.getDepartmentId().equals(departmentId)).findFirst();
        if (CollectionUtils.isEmpty(employeeEntityList)) {
            return ResponseDTO.ok(Collections.emptyList());
        }
        List<EmployeeVO> voList = employeeEntityList.stream().map(e -> {
            EmployeeVO employeeVO = SmartBeanUtil.copy(e, EmployeeVO.class);
            if (departmentVO.isPresent()) {
                employeeVO.setDepartmentName(departmentVO.get().getName());
            }
            return employeeVO;
        }).collect(Collectors.toList());
        return ResponseDTO.ok(voList);
    }


    /**
     * 重置密码
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<String> resetPassword(Integer employeeId) {
        employeeDao.updatePassword(employeeId, getEncryptPwd(DEFAULT_PASSWORD));
        return ResponseDTO.okMsg("重置密码为：" + DEFAULT_PASSWORD);
    }


    /**
     * 获取 加密后 的密码
     *
     * @param password
     * @return
     */
    public static String getEncryptPwd(String password) {
        return DigestUtils.md5Hex(String.format(PASSWORD_SALT_FORMAT, password));
    }

    /**
     * 查询全部员工
     *
     * @return
     */
    public ResponseDTO<List<EmployeeVO>> queryAllEmployee(Boolean disabledFlag, RequestEmployee requestEmployee) {
        EmployeeQueryForm employeeQueryForm = new EmployeeQueryForm();
        employeeQueryForm.setDisabledFlag(disabledFlag);
        List<EmployeeVO> employeeList = employeeDao.queryEmployee(employeeQueryForm);
        return ResponseDTO.ok(employeeList);
    }
}
