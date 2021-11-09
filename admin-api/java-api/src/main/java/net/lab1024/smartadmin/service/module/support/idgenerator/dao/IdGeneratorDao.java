package net.lab1024.smartadmin.service.module.support.idgenerator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * id生成 dao
 *
 * @author zhuo
 */
@Mapper
@Component
public interface IdGeneratorDao extends BaseMapper<IdGeneratorEntity> {

    /**
     * 排他锁查询
     * @param id
     * @return
     */
    IdGeneratorEntity selectForUpdate(@Param("id")Integer id);
}
