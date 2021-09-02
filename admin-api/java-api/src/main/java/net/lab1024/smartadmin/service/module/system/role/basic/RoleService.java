package net.lab1024.smartadmin.service.module.system.role.basic;

import net.lab1024.smartadmin.service.common.codeconst.ResponseCodeConst;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.system.role.basic.domain.dto.RoleAddDTO;
import net.lab1024.smartadmin.service.module.system.role.basic.domain.dto.RoleUpdateDTO;
import net.lab1024.smartadmin.service.module.system.role.basic.domain.entity.RoleEntity;
import net.lab1024.smartadmin.service.module.system.role.basic.domain.vo.RoleVO;
import net.lab1024.smartadmin.service.module.system.role.roleemployee.RoleEmployeeDao;
import net.lab1024.smartadmin.service.module.system.role.rolemenu.RoleMenuDao;
import net.lab1024.smartadmin.service.util.SmartBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
     * @param roleAddDTO
     * @return RespDTO
     */
    public ResponseDTO addRole(RoleAddDTO roleAddDTO) {
        RoleEntity employeeRoleEntity = roleDao.getByRoleName(roleAddDTO.getRoleName());
        if (null != employeeRoleEntity) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ALREADY_EXIST, "角色名称重复");
        }
        RoleEntity roleEntity = SmartBeanUtil.copy(roleAddDTO, RoleEntity.class);
        roleDao.insert(roleEntity);
        return ResponseDTO.succ();
    }

    /**
     * 根据角色id 删除
     *
     * @param roleId
     * @return RespDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO deleteRole(Long roleId) {
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (null == roleEntity) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        roleDao.deleteById(roleId);
        roleMenuDao.deleteByRoleId(roleId);
        roleEmployeeDao.deleteByRoleId(roleId);
        return ResponseDTO.succ();
    }

    /**
     * 更新角色
     *
     * @param roleUpdateDTO
     * @return RespDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateRole(RoleUpdateDTO roleUpdateDTO) {
        Long roleId = roleUpdateDTO.getId();
        if (null == roleDao.selectById(roleId)) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        RoleEntity employeeRoleEntity = roleDao.getByRoleName(roleUpdateDTO.getRoleName());
        if (null != employeeRoleEntity && !Objects.equals(employeeRoleEntity.getId(), roleId)) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ALREADY_EXIST, "角色名称重复");
        }
        RoleEntity roleEntity = SmartBeanUtil.copy(roleUpdateDTO, RoleEntity.class);
        roleDao.updateById(roleEntity);
        return ResponseDTO.succ();
    }

    /**
     * 根据id获取角色数据
     *
     * @param roleId
     * @return RespDTO
     */
    public ResponseDTO<RoleVO> getRoleById(Long roleId) {
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (null == roleEntity) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        RoleVO role = SmartBeanUtil.copy(roleEntity, RoleVO.class);
        return ResponseDTO.succData(role);
    }

    /**
     * 获取所有角色列表
     *
     * @return RespDTO
     */
    public ResponseDTO<List<RoleVO>> getAllRole() {
        List<RoleEntity> roleEntityList = roleDao.selectList(null);
        List<RoleVO> roleList = SmartBeanUtil.copyList(roleEntityList, RoleVO.class);
        return ResponseDTO.succData(roleList);
    }
}
