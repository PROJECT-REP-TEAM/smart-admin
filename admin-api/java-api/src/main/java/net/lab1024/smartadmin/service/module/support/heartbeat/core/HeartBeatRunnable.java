package net.lab1024.smartadmin.service.module.support.heartbeat.core;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/9/23 18:52
 */
public class HeartBeatRunnable implements Runnable {

    private IHeartBeatRecordHandler recordHandler;

    private HeartBeatServer server;

    public HeartBeatRunnable(IHeartBeatRecordHandler recordHandler, HeartBeatServer server) {
        this.recordHandler = recordHandler;
        this.server = server;
    }

    @Override
    public void run() {
        HeartBeatRecord heartBeatRecord = new HeartBeatRecord();
        heartBeatRecord.setProjectPath(server.getProjectPath());
        heartBeatRecord.setServerIp(StringUtils.join(server.getServerIps(), ";"));
        heartBeatRecord.setProcessNo(server.getProcessNo());
        heartBeatRecord.setProcessStartTime(server.getProcessStartTime());
        heartBeatRecord.setHeartBeatTime(new Date());
        recordHandler.handler(heartBeatRecord);
    }
}
