package net.lab1024.smartadmin.service.module.support.serialnumber.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.exception.BusinessException;
import net.lab1024.smartadmin.service.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.smartadmin.service.module.support.serialnumber.domain.SerialNumberEntity;
import org.springframework.aop.framework.AopContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhuoda
 * @Date 2021-11-10
 */

@Slf4j
public class SerialNumberMysqlService extends SerialNumberBaseService {

    private static final int MAX_GET_LOCK_COUNT = 5;

    private static final long SLEEP_MILLISECONDS = 500L;

    private static volatile long lastSleepMilliSeconds = SLEEP_MILLISECONDS;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String generate(SerialNumberIdEnum serialNumberIdEnum) {
        List<String> generateList = this.generate(serialNumberIdEnum, 1);
        if (generateList == null || generateList.isEmpty()) {
            throw new BusinessException("cannot generate SerialNumber : " + serialNumberIdEnum.toString());
        }
        return generateList.get(0);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public List<String> generate(SerialNumberIdEnum serialNumberIdEnum, int count) {
        SerialNumberEntity serialNumberEntity = serialNumberCacheManager.getSerialNumber(serialNumberIdEnum.getSerialNumberId());
        if (serialNumberEntity == null) {
            throw new BusinessException("cannot generate SerialNumber : " + serialNumberIdEnum.toString());
        }
        SerialNumberBaseService proxyService = (SerialNumberBaseService) AopContext.currentProxy();
        return proxyService.tryGenerator(serialNumberIdEnum, count);
    }

    @Override
    List<String> tryGenerator(SerialNumberIdEnum serialNumberIdEnum, int count) {
        SerialNumberEntity serialNumberEntity = serialNumberDao.selectForUpdate(serialNumberIdEnum.getValue());
        if (serialNumberEntity == null) {
            throw new BusinessException("cannot found SerialNumberId 数据库不存在:" + serialNumberIdEnum);
        }
        return generate(serialNumberIdEnum, count);
    }
}
