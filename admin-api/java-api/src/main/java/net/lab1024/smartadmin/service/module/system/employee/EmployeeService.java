package net.lab1024.smartadmin.service.module.system.employee;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.constant.CacheModuleConst;
import net.lab1024.smartadmin.service.common.constant.StringConst;
import net.lab1024.smartadmin.service.common.domain.PageResultDTO;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.support.beancache.cache.IBeanCache;
import net.lab1024.smartadmin.service.module.support.beancache.key.CacheKey;
import net.lab1024.smartadmin.service.module.system.department.DepartmentDao;
import net.lab1024.smartadmin.service.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.smartadmin.service.module.system.department.domain.vo.DepartmentVO;
import net.lab1024.smartadmin.service.module.system.employee.domain.dto.*;
import net.lab1024.smartadmin.service.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.smartadmin.service.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginBO;
import net.lab1024.smartadmin.service.module.system.login.domain.EmployeeLoginInfoDTO;
import net.lab1024.smartadmin.service.module.system.menu.MenuEmployeeService;
import net.lab1024.smartadmin.service.module.system.role.roleemployee.RoleEmployeeDao;
import net.lab1024.smartadmin.service.module.system.role.roleemployee.domain.RoleEmployeeEntity;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.common.util.SmartPageUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    protected IBeanCache beanCache;

    /**
     * 获取员工登录信息
     *
     * @param employeeId
     * @return
     */
    public EmployeeLoginInfoDTO getById(Long employeeId) {
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Employee.SINGLE_EMPLOYEE_CACHE, employeeId.toString());
        EmployeeEntity employeeEntity = beanCache.get(cacheKey);
        //获取员工角色缓存
        String roleCacheKey = CacheKey.cacheKey(CacheModuleConst.Employee.SINGLE_EMPLOYEE_ROLE_CACHE, employeeId.toString());
        List<Long> roleIdList = beanCache.get(roleCacheKey);
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
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Employee.SINGLE_EMPLOYEE_CACHE, employeeId.toString());
        EmployeeEntity employeeEntity = beanCache.get(cacheKey);
        //获取员工角色缓存
        String roleCacheKey = CacheKey.cacheKey(CacheModuleConst.Employee.SINGLE_EMPLOYEE_ROLE_CACHE, employeeId.toString());
        List<Long> roleIdList = beanCache.get(roleCacheKey);
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
    public ResponseDTO<PageResultDTO<EmployeeVO>> queryEmployeeList(EmployeeQueryDTO queryDTO) {
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

        this.clearCacheByDepartmentId(departmentId);

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
        this.clearCacheByEmployeeId(employeeId);
        this.clearCacheByDepartmentId(departmentId);

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

        this.clearCacheByEmployeeId(employeeId);
        this.clearCacheByDepartmentId(employeeEntity.getDepartmentId());
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
            this.clearCacheByEmployeeId(e.getId());
            this.clearCacheByDepartmentId(e.getDepartmentId());
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
            this.clearCacheByEmployeeId(e.getId());
            this.clearCacheByDepartmentId(e.getDepartmentId());
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

        this.clearCacheByEmployeeId(employeeId);
        this.clearCacheByDepartmentId(employeeEntity.getDepartmentId());
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
            this.clearCacheByEmployeeId(e.getId());
            this.clearCacheByDepartmentId(e.getDepartmentId());
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
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Employee.DEPARTMENT_EMPLOYEE_CACHE, departmentId.toString());
        List<EmployeeEntity> employeeEntityList = beanCache.get(cacheKey);
        if (CollectionUtils.isEmpty(employeeEntityList)) {
            return ResponseDTO.ok(StringConst.EMPTY_LIST);
        }
        List<EmployeeVO> voList = SmartBeanUtil.copyList(employeeEntityList, EmployeeVO.class);
        return ResponseDTO.ok(voList);
    }

    /**
     * 获取某个校区的员工信息
     *
     * @param departmentId
     * @return
     */
    public ResponseDTO<List<EmployeeVO>> getEmployeeBySchoolId(Long departmentId) {
        // 查询部门下所有部门包含子部门
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Department.DEPARTMENT_CACHE);
        List<DepartmentVO> departmentList = beanCache.get(cacheKey);
        // 先查询本部门的员工
        ResponseDTO<List<EmployeeVO>> employeeByDeptId = getEmployeeByDeptId(departmentId);
        List<EmployeeVO> schoolEmployeeList = employeeByDeptId.getData();
        // 进入递归
        List<EmployeeVO> employeeList = Lists.newArrayList(schoolEmployeeList);
        recursionFindEmployee(employeeList, departmentList, departmentId);
        return ResponseDTO.ok(employeeList);
    }

    /**
     * 递归查询员工
     *
     * @param employeeList
     * @param departmentList
     */
    private void recursionFindEmployee(List<EmployeeVO> employeeList, List<DepartmentVO> departmentList, Long parentId) {
        // 寻找子集部门
        List<DepartmentVO> childDepartmentList = departmentList.stream().filter(e -> e.getParentId().equals(parentId)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(childDepartmentList)) {
            return;
        }
        for (DepartmentVO departmentVO : childDepartmentList) {
            // 查询本级部门下包含的员工
            ResponseDTO<List<EmployeeVO>> employeeByDeptId = getEmployeeByDeptId(departmentVO.getId());
            employeeList.addAll(employeeByDeptId.getData());
            recursionFindEmployee(employeeList, departmentList, departmentVO.getId());
        }
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
     * 清除businessId为员工id的缓存信息
     *
     * @param employeeId
     */
    public void clearCacheByEmployeeId(Long employeeId) {
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Employee.SINGLE_EMPLOYEE_CACHE, employeeId.toString());
        beanCache.remove(cacheKey);
        String roleCacheKey = CacheKey.cacheKey(CacheModuleConst.Employee.SINGLE_EMPLOYEE_ROLE_CACHE, employeeId.toString());
        beanCache.remove(roleCacheKey);
    }

    /**
     * 清除businessId为部门id的缓存信息
     *
     * @param departmentId
     */
    private void clearCacheByDepartmentId(Long departmentId) {
        String cacheKey = CacheKey.cacheKey(CacheModuleConst.Employee.DEPARTMENT_EMPLOYEE_CACHE, departmentId.toString());
        beanCache.remove(cacheKey);
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
        EmployeeQueryDTO queryDTO = new EmployeeQueryDTO();
        queryDTO.setDeletedFlag(Boolean.FALSE);
        if (disabledFlag != null) {
            queryDTO.setDisabledFlag(disabledFlag);
        }
        List<EmployeeVO> employeeList = employeeDao.queryEmployee(queryDTO);
        return ResponseDTO.ok(employeeList);
    }
}
