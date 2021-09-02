package net.lab1024.smartadmin.service.module.support.heartbeat.core;

/**
 * [  ]
 *
 * @author 罗伊
 */
public interface IHeartBeatRecordHandler {

    /**
     * 心跳日志处理方法
     *
     * @param recordDTO
     */
    void handler(HeartBeatRecordDTO recordDTO);
}
