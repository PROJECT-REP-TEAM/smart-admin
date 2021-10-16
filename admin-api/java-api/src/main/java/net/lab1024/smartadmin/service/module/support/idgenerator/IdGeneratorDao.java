package net.lab1024.smartadmin.service.module.support.idgenerator;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * id生成 dao
 *
 * @author zhuo
 */
@Mapper
@Component
public interface IdGeneratorDao extends BaseMapper<IdGeneratorEntity> {

}
