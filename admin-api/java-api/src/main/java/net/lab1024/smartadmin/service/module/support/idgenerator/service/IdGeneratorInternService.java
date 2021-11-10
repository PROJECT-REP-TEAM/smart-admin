package net.lab1024.smartadmin.service.module.support.idgenerator.service;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuoda
 * @Date 2021-11-10
 */

@Service
public class IdGeneratorInternService extends IdGeneratorBaseService {

    private static final Interner<Integer> POOL = Interners.newWeakInterner();

    @Override
    List<String> tryGenerator(IdGeneratorEnum idGeneratorEnum, int count) {
        IdGeneratorEntity idGeneratorEntity = idGeneratorCacheManager.getIdGeneratorEntity(idGeneratorEnum.getValue());
        synchronized (POOL.intern(idGeneratorEntity.getId())) {
            return generateIdList(idGeneratorEntity, count);
        }
    }
}
