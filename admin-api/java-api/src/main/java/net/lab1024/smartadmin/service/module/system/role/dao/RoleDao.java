package net.lab1024.smartadmin.service.module.system.role.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import net.lab1024.smartadmin.service.module.system.role.domain.entity.RoleEntity;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2019/3/27 0027 下午 13:00
 */
@Mapper
@Component
public interface RoleDao extends BaseMapper<RoleEntity> {

    /**
     * 根据角色名称查询
     * @param roleName
     * @return
     */
    RoleEntity getByRoleName(@Param("roleName") String roleName);

}
