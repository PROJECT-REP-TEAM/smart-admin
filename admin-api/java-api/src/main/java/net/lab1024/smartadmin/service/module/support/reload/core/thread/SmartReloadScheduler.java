package net.lab1024.smartadmin.service.module.support.reload.core.thread;

import net.lab1024.smartadmin.service.module.support.reload.core.AbstractSmartReloadCommand;
import net.lab1024.smartadmin.service.module.support.reload.core.SmartReloadLogger;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Reload 调度器
 *
 * @author zhuoda
 */
public class SmartReloadScheduler {

    private ScheduledThreadPoolExecutor executor;

    private SmartReloadLogger logger;


    public SmartReloadScheduler(SmartReloadLogger logger, int threadCount) {
        this.executor = new ScheduledThreadPoolExecutor(threadCount, new SmartReloadThreadFactory());
        this.logger = logger;
    }


    public void shutdown() {
        try {
            executor.shutdown();
        } catch (Throwable e) {
            logger.error("<<SmartReloadScheduler>> shutdown ", e);
        }
    }

    public void addCommand(AbstractSmartReloadCommand command, long initialDelay, long delay, TimeUnit unit) {
        executor.scheduleWithFixedDelay(new SmartReloadRunnable(command, this.logger), initialDelay, delay, unit);
    }

    public void addCommand(AbstractSmartReloadCommand command) {
        executor.scheduleWithFixedDelay(new SmartReloadRunnable(command, this.logger), 10, 20, TimeUnit.SECONDS);
    }


}
