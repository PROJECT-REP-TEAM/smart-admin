package net.lab1024.smartadmin.service.module.support.idgenerator.strategy;

import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorStrategyTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/11/9 18:48
 */
@Service
public class IdGeneratorStrategyFactory {

    @Autowired
    private List<IIdGeneratorStrategy> idGeneratorStrategyList;

    /**
     * 获取某个策略
     * @param strategyTypeEnum
     * @return
     */
    public IIdGeneratorStrategy getIdGeneratorStrategy(IdGeneratorStrategyTypeEnum strategyTypeEnum) {
        Optional<IIdGeneratorStrategy> idGeneratorStrategyOptional = idGeneratorStrategyList.stream().filter(e -> e.getStrategyType() == strategyTypeEnum).findFirst();
        if (!idGeneratorStrategyOptional.isPresent()) {
            return null;
        }
        return idGeneratorStrategyOptional.get();
    }
}
