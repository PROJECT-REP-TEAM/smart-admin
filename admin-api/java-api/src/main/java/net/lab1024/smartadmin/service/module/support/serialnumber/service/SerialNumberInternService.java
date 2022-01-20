package net.lab1024.smartadmin.service.module.support.serialnumber.service;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import net.lab1024.smartadmin.service.module.support.serialnumber.domain.SerialNumberBO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuoda
 * @Date 2021-11-10
 */

@Service
public class SerialNumberInternService extends SerialNumberBaseService {

    private static final Interner<Integer> POOL = Interners.newWeakInterner();

    @Override
    List<Long> generateSerialNumberList(SerialNumberBO serialNumber, int count) {
        synchronized (POOL.intern(serialNumber.getSerialNumberId())) {
            List<Long> numberList = loopNumberList(serialNumber, count);
            /**
             *  更新主表：上次number和时间
             *  为什么要在 同步块里执行呢？？
             *  因为只有在这里才能保证是同步的操作数据库
             */
            serialNumberDao.updateLastNumberAndTime(serialNumber.getSerialNumberId(),
                    serialNumber.getLastNumber(),
                    serialNumber.getLastTime());

            return numberList;
        }

        /**
         * 更新记录
         * 为什么操作记录 可以放在 同步块之外呢，因为是进行 c
          */



    }
}
