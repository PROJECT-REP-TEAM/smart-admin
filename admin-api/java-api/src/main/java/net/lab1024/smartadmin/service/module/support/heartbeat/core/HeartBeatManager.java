package net.lab1024.smartadmin.service.module.support.heartbeat.core;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Slf4j
public class HeartBeatManager extends AbstractHeartBeatManager {

    private IHeartBeatRecordHandler heartBeatRecordHandler;

    /**
     * @param delayHandlerTime 延迟执行时间
     * @param intervalTime     间隔执行时间
     */
    public HeartBeatManager(Long delayHandlerTime, Long intervalTime, IHeartBeatRecordHandler heartBeatRecordHandler) {
        HeartBeatConfig config = HeartBeatConfig.builder().delayHandlerTime(delayHandlerTime).intervalTime(intervalTime).build();
        super.init(config, new HeartBeatLogger() {
            @Override
            public void error(String string) {
                log.error(string);
            }

            @Override
            public void error(String string, Throwable e) {
                log.error(string, e);
            }

            @Override
            public void info(String string) {
                log.info(string);
            }
        });
        this.heartBeatRecordHandler = heartBeatRecordHandler;
    }


    @PreDestroy
    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void handler(HeartBeatRecordDTO heartBeatRecordDTO) {
        heartBeatRecordHandler.handler(heartBeatRecordDTO);
    }

}
