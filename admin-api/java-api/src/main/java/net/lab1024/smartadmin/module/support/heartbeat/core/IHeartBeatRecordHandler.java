package net.lab1024.smartadmin.module.support.heartbeat.core;

/**
 * [  ]
 *
 * @author 罗伊
 */
public interface IHeartBeatRecordHandler {

    /**
     * 心跳日志处理方法
     *
     * @param heartBeatRecord
     */
    void handler(HeartBeatRecord heartBeatRecord);
}
