package net.lab1024.smartadmin.service.module.system.role.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.system.role.dao.RoleDao;
import net.lab1024.smartadmin.service.module.system.role.domain.form.RoleAddForm;
import net.lab1024.smartadmin.service.module.system.role.domain.form.RoleUpdateForm;
import net.lab1024.smartadmin.service.module.system.role.domain.entity.RoleEntity;
import net.lab1024.smartadmin.service.module.system.role.domain.vo.RoleVO;
import net.lab1024.smartadmin.service.module.system.role.dao.RoleEmployeeDao;
import net.lab1024.smartadmin.service.module.system.role.dao.RoleMenuDao;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;

import java.util.List;

/**
 * 角色管理业务
 *
 * @author listen
 * @date 2017/12/28 09:37
 */
@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleMenuDao roleMenuDao;

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;

    /**
     * 新增添加角色
     *
     * @param roleAddForm
     * @return ResponseDTO
     */
    public ResponseDTO addRole(RoleAddForm roleAddForm) {
        RoleEntity employeeRoleEntity = roleDao.getByRoleName(roleAddForm.getRoleName());
        if (null != employeeRoleEntity) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "角色名称重复");
        }
        RoleEntity roleEntity = SmartBeanUtil.copy(roleAddForm, RoleEntity.class);
        roleDao.insert(roleEntity);
        return ResponseDTO.ok();
    }

    /**
     * 根据角色id 删除
     *
     * @param roleId
     * @return ResponseDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO deleteRole(Long roleId) {
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (null == roleEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        roleDao.deleteById(roleId);
        roleMenuDao.deleteByRoleId(roleId);
        roleEmployeeDao.deleteByRoleId(roleId);
        return ResponseDTO.ok();
    }

    /**
     * 更新角色
     *
     * @param roleUpdateForm
     * @return ResponseDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateRole(RoleUpdateForm roleUpdateForm) {
        if (null == roleDao.selectById(roleUpdateForm.getId())) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        RoleEntity employeeRoleEntity = roleDao.getByRoleName(roleUpdateForm.getRoleName());
        if (null != employeeRoleEntity && !employeeRoleEntity.getId().equals(roleUpdateForm.getId())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "角色名称重复");
        }
        RoleEntity roleEntity = SmartBeanUtil.copy(roleUpdateForm, RoleEntity.class);
        roleDao.updateById(roleEntity);
        return ResponseDTO.ok();
    }

    /**
     * 根据id获取角色数据
     *
     * @param roleId
     * @return ResponseDTO
     */
    public ResponseDTO<RoleVO> getRoleById(Long roleId) {
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (null == roleEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        RoleVO role = SmartBeanUtil.copy(roleEntity, RoleVO.class);
        return ResponseDTO.ok(role);
    }

    /**
     * 获取所有角色列表
     *
     * @return ResponseDTO
     */
    public ResponseDTO<List<RoleVO>> getAllRole() {
        List<RoleEntity> roleEntityList = roleDao.selectList(null);
        List<RoleVO> roleList = SmartBeanUtil.copyList(roleEntityList, RoleVO.class);
        return ResponseDTO.ok(roleList);
    }
}
