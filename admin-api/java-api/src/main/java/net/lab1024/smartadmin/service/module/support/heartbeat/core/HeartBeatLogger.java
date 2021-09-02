package net.lab1024.smartadmin.service.module.support.heartbeat.core;

/**
 * [  ]
 *
 * @author 罗伊
 */
public interface HeartBeatLogger {

    void error(String string);

    void error(String string, Throwable e);

    void info(String string);
}
