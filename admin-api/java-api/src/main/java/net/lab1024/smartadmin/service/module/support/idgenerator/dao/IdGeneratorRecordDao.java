package net.lab1024.smartadmin.service.module.support.idgenerator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorRecordEntity;
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
public interface IdGeneratorRecordDao extends BaseMapper<IdGeneratorRecordEntity> {

    /**
     * 查询id最后生成记录
     *
     * @param generatorId
     * @param timeFormat
     * @return
     */
    IdGeneratorRecordEntity selectHistoryLastNumber(@Param("generatorId") Integer generatorId, @Param("time") String timeFormat);
}
