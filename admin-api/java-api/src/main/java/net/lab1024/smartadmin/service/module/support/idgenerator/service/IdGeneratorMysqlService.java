package net.lab1024.smartadmin.service.module.support.idgenerator.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.exception.BusinessException;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorEntity;
import org.springframework.aop.framework.AopContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhuoda
 * @Date 2021-11-10
 */

@Slf4j
public class IdGeneratorMysqlService extends IdGeneratorBaseService {

    private static final int MAX_GET_LOCK_COUNT = 5;

    private static final long SLEEP_MILLISECONDS = 500L;

    private static volatile long lastSleepMilliSeconds = SLEEP_MILLISECONDS;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String generate(IdGeneratorEnum idGeneratorEnum) {
        List<String> generateList = this.generate(idGeneratorEnum, 1);
        if (generateList == null || generateList.isEmpty()) {
            throw new BusinessException("cannot generate id : " + idGeneratorEnum.toString());
        }
        return generateList.get(0);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public List<String> generate(IdGeneratorEnum idGeneratorEnum, int count) {
        IdGeneratorEntity idGeneratorEntity = idGeneratorCacheManager.getIdGeneratorEntity(idGeneratorEnum.getIdGeneratorId());
        if (idGeneratorEntity == null) {
            throw new BusinessException("cannot found id generator : " + idGeneratorEnum.toString());
        }
        IdGeneratorBaseService proxyService = (IdGeneratorBaseService) AopContext.currentProxy();
        return proxyService.tryGenerator(idGeneratorEnum, count);
    }

    @Override
    List<String> tryGenerator(IdGeneratorEnum idGeneratorEnum, int count) {
        IdGeneratorEntity idGeneratorEntity = idGeneratorDao.selectForUpdate(idGeneratorEnum.getValue());
        if (idGeneratorEntity == null) {
            throw new BusinessException("IdGenerator， id 数据库不存在");
        }
        return generate(idGeneratorEnum, count);
    }
}
