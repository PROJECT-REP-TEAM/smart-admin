package net.lab1024.smartadmin.service.module.support.idgenerator.strategy;

import net.lab1024.smartadmin.service.common.util.SmartRandomUtil;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorRuleTypeEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorStrategyTypeEnum;
import net.lab1024.smartadmin.service.module.support.idgenerator.domain.IdGeneratorRecordEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/11/9 18:48
 */
public interface IIdGeneratorStrategy {

    /**
     * 策略类型
     * @return
     */
    IdGeneratorStrategyTypeEnum getStrategyType();

    /**
     * 生成
     * @param idGeneratorEnum
     * @return
     */
    String generate(IdGeneratorEnum idGeneratorEnum);


}
