package net.lab1024.smartadmin.service.module.system.role.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import net.lab1024.smartadmin.service.module.system.role.domain.entity.RoleMenuEntity;

import java.util.List;

/**
 * 角色-菜单
 *
 * @author lihaifan
 * @date 2021/7/30 17:18
 */
@Mapper
@Component
public interface RoleMenuDao extends BaseMapper<RoleMenuEntity> {

    /**
     * 根据角色ID删除菜单权限
     * @param roleId
     */
    void deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色ID查询选择的菜单权限
     * @param roleId
     * @return
     */
    List<Long> queryMenuIdByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询所有的菜单角色
     * @return
     */
    List<RoleMenuEntity> queryAllRoleMenu();
}
