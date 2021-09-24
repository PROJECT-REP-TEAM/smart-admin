package net.lab1024.smartadmin.service.module.support.reload.interfaces;

/**
 * SmartReloadThreadLogger 日志类
 */
public interface SmartReloadThreadLogger {

    void error(String string);

    void error(String string, Throwable e);

}
