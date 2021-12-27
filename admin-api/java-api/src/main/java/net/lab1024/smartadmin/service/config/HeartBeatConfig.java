package net.lab1024.smartadmin.service.config;

import net.lab1024.smartadmin.service.module.support.heartbeat.core.HeartBeatManager;
import net.lab1024.smartadmin.service.module.support.heartbeat.core.IHeartBeatRecordHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/10/9 18:47
 */
@Configuration
public class HeartBeatConfig {

    /**
     * 间隔时间
     */
    @Value("${heart-beat.interval-seconds}")
    private Long intervalSeconds;

    @Autowired
    private IHeartBeatRecordHandler heartBeatRecordHandler;

    @Bean
    public HeartBeatManager heartBeatManager() {
        return new HeartBeatManager(intervalSeconds * 1000L, heartBeatRecordHandler);
    }


}
