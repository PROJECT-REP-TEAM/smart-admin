package net.lab1024.smartadmin.service.module.support.idgenerator.strategy;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.exception.BusinessException;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorStrategyTypeEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.dao.IdGeneratorDao;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/11/9 18:48
 */
@Slf4j
@Service
public class IdGeneratorMySqlStrategy extends IdGeneratorStrategyBaseService {


    @Autowired
    private IdGeneratorDao idGeneratorDao;

    /**
     * 策略类型
     *
     * @return
     */
    @Override
    public IdGeneratorStrategyTypeEnum getStrategyType() {
        return IdGeneratorStrategyTypeEnum.MYSQL_LOCK;
    }

    /**
     * 生成
     *
     * @param idGeneratorEnum
     * @return
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String generate(IdGeneratorEnum idGeneratorEnum) {
        IdGeneratorEntity idGeneratorEntity = idGeneratorDao.selectForUpdate(idGeneratorEnum.getValue());
        if (idGeneratorEntity == null) {
            throw new BusinessException("IdGenerator， id 数据库不存在");
        }
        return generate(idGeneratorEntity);

    }
}
