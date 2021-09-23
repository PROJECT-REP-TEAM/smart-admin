package net.lab1024.smartadmin.service.module.support.heartbeat.core;

import net.lab1024.smartadmin.service.util.SmartIPUtil;
import net.lab1024.smartadmin.service.util.SmartThreadFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * [  ]
 *
 * @author 罗伊
 */
public class HeartBeatManager {

    private ScheduledThreadPoolExecutor threadPoolExecutor;

    /**
     * 服务状态持久化处理类
     */
    private IHeartBeatRecordHandler heartBeatRecordHandler;

    /**
     * 调度配置信息
     */
    private HeartBeatConfig config;

    /**
     * 服务信息
     */
    private HeartBeatServer server;

    /**
     * @param delayHandlerTime 延迟执行时间
     * @param intervalTime     间隔执行时间
     */
    public HeartBeatManager(Long delayHandlerTime,
                            Long intervalTime,
                            IHeartBeatRecordHandler heartBeatRecordHandler) {
        this.config = HeartBeatConfig.builder().delayHandlerTime(delayHandlerTime).intervalTime(intervalTime).build();
        this.heartBeatRecordHandler = heartBeatRecordHandler;
        this.server = handlerHeartServer();
        this.threadPoolExecutor = new ScheduledThreadPoolExecutor(1, new HeartBeatThreadFactory());
        this.heartBeatScheduler();
    }

    /**
     * 调度监控服务状态
     */
    private void heartBeatScheduler() {
        HeartBeatRunnable heartBeatRunnable = new HeartBeatRunnable(heartBeatRecordHandler,server);
        threadPoolExecutor.scheduleWithFixedDelay(heartBeatRunnable, config.getDelayHandlerTime(), config.getIntervalTime(), TimeUnit.MILLISECONDS);
    }

    /**
     * 服务信息
     * @return
     */
    private HeartBeatServer handlerHeartServer(){
        HeartBeatServer server = new HeartBeatServer();
        server.setProjectPath(HeatBeatRecordHelper.getProjectPath());
        server.setServerIps(SmartIPUtil.getLocalHostIPList());
        server.setProcessNo(HeatBeatRecordHelper.getProcessID());
        server.setProcessStartTime(HeatBeatRecordHelper.getStartTime());
        return server;
    }

}
