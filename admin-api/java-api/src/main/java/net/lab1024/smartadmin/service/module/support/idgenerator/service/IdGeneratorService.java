package net.lab1024.smartadmin.service.module.support.idgenerator.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorStrategyTypeEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.strategy.IdGeneratorStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 全局id生成器
 *
 * @author zhuo
 */
@Slf4j
@Service
public class IdGeneratorService {

    @Autowired
    private IdGeneratorStrategyFactory idGeneratorStrategyFactory;

    /**
     * 自定义生成策略生成
     *
     * @param idGeneratorEnum
     * @param strategyTypeEnum
     * @return
     */
    public String generate(IdGeneratorEnum idGeneratorEnum, IdGeneratorStrategyTypeEnum strategyTypeEnum) {
        return idGeneratorStrategyFactory.getIdGeneratorStrategy(strategyTypeEnum).generate(idGeneratorEnum);
    }

    /**
     * 简单的生成 依据 intern
     *
     * @param idGeneratorEnum
     * @return
     */
    public String simpleGenerate(IdGeneratorEnum idGeneratorEnum) {
        return idGeneratorStrategyFactory.getIdGeneratorStrategy(IdGeneratorStrategyTypeEnum.INTERN).generate(idGeneratorEnum);
    }


}
