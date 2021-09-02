package net.lab1024.smartadmin.service.module.system.role.basic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import net.lab1024.smartadmin.service.module.system.role.basic.domain.entity.RoleEntity;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Mapper
@Component
public interface RoleDao extends BaseMapper<RoleEntity> {


    RoleEntity getByRoleName(@Param("roleName") String name);

}
