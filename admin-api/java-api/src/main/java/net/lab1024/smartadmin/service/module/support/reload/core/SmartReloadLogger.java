package net.lab1024.smartadmin.service.module.support.reload.core;

/**
 * SmartReloadThreadLogger 日志类
 */
public interface SmartReloadLogger {

    void error(String string);

    void error(String string, Throwable e);

}
