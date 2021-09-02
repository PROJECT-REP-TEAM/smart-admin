package net.lab1024.smartadmin.service.module.support.idgenerator;

import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorEntity;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorRecordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * zhuo
 */
@Mapper
@Component
public interface IdGeneratorDao {

    List<IdGeneratorEntity> selectAll();

    int replaceIdGeneratorRecord(@Param("generatorId") Integer generatorId,
                                 @Param("year") int year,
                                 @Param("month") int month,
                                 @Param("day") int day,
                                 @Param("lastNumber") Long lastNumber,
                                 @Param("count") long count);

    IdGeneratorRecordDTO selectHistoryLastNumber(@Param("generatorId") Integer generatorId,
                                                 @Param("year") int year,
                                                 @Param("month") int month,
                                                 @Param("day") int day);

}
