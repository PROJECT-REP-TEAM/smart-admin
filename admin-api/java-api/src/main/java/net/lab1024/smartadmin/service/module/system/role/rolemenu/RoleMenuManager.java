package net.lab1024.smartadmin.service.module.system.role.rolemenu;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.lab1024.smartadmin.service.module.system.role.rolemenu.domain.RoleMenuEntity;

import java.util.List;

/**
 * 角色-菜单
 *
 * @author 善逸
 * @date 2021/7/30 17:13
 */
@Service
public class RoleMenuManager extends ServiceImpl<RoleMenuDao, RoleMenuEntity> {

    @Autowired
    private RoleMenuDao roleMenuDao;

    /**
     * 更新角色权限
     * @param roleId
     * @param roleMenuEntityList
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleMenu(Long roleId, List<RoleMenuEntity> roleMenuEntityList) {
        // 根据角色ID删除菜单权限
        roleMenuDao.deleteByRoleId(roleId);
        // 批量添加菜单权限
        saveBatch(roleMenuEntityList);
    }
}
