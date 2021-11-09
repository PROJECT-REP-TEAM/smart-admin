package net.lab1024.smartadmin.service.module.support.idgenerator.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.constant.CacheModuleConst;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.dao.IdGeneratorDao;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 
 * [  ]
 * 
 * @author yandanyang
 * @date
 */
@Slf4j
@Service
public class IdGeneratorCacheService {

    @Autowired
    private IdGeneratorDao idGeneratorDao;

    @Cacheable(CacheModuleConst.IdGenerator.ID_GENERATOR_CACHE)
    public IdGeneratorEntity getIdGeneratorEntity(Integer generatorId){
        return idGeneratorDao.selectById(generatorId);
    }

}
