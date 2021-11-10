package net.lab1024.smartadmin.service.module.support.idgenerator.strategy;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorStrategyTypeEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorEntity;
import net.lab1024.smartadmin.service.module.support.idgenerator.service.IdGeneratorCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/11/9 18:48
 */
@Service
public class IdGeneratorInternStrategy extends IdGeneratorStrategyBaseService {

    private static final Interner<Integer> POOL = Interners.newWeakInterner();
    @Autowired
    private IdGeneratorCacheManager idGeneratorCacheManager;

    /**
     * 策略类型
     *
     * @return
     */
    @Override
    public IdGeneratorStrategyTypeEnum getStrategyType() {
        return IdGeneratorStrategyTypeEnum.INTERN;
    }

    /**
     * 生成
     *
     * @param idGeneratorEnum
     * @return
     */
    @Override
    public String generate(IdGeneratorEnum idGeneratorEnum) {
        IdGeneratorEntity idGeneratorEntity = idGeneratorCacheManager.getIdGeneratorEntity(idGeneratorEnum.getValue());
        synchronized (POOL.intern(idGeneratorEntity.getId())) {
            return generate(idGeneratorEntity);
        }
    }
}
