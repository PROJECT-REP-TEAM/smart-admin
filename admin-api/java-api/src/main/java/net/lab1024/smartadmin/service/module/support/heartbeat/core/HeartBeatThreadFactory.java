package net.lab1024.smartadmin.service.module.support.heartbeat.core;

import java.util.concurrent.ThreadFactory;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/9/23 20:19
 */
public class HeartBeatThreadFactory implements ThreadFactory {

    private static String namePrefix = "heart-beat";

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, namePrefix);
        if (!t.isDaemon()) {
            t.setDaemon(true);
        }
        return t;
    }
}