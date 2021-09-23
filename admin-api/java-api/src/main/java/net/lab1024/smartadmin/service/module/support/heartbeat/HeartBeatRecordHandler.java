package net.lab1024.smartadmin.service.module.support.heartbeat;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.module.support.heartbeat.core.HeartBeatRecord;
import net.lab1024.smartadmin.service.module.support.heartbeat.core.IHeartBeatRecordHandler;
import net.lab1024.smartadmin.service.util.SmartBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Slf4j
@Service
public class HeartBeatRecordHandler implements IHeartBeatRecordHandler {

    @Autowired
    private HeartBeatRecordDao heartBeatRecordDao;

    /**
     * 心跳日志处理方法
     * @param heartBeatRecord
     */
    @Override
    public void handler(HeartBeatRecord heartBeatRecord) {
        HeartBeatRecordEntity heartBeatRecordEntity = SmartBeanUtil.copy(heartBeatRecord, HeartBeatRecordEntity.class);
        HeartBeatRecordEntity heartBeatRecordOld = heartBeatRecordDao.query(heartBeatRecordEntity);
        if (heartBeatRecordOld == null) {
            heartBeatRecordDao.insertHeartBeat(heartBeatRecordEntity);
        } else {
            heartBeatRecordDao.updateHeartBeatTimeById(heartBeatRecordOld.getId(), heartBeatRecordEntity.getHeartBeatTime());
        }
    }

}
