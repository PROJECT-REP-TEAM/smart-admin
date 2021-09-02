package net.lab1024.smartadmin.service.config;

import net.lab1024.smartadmin.service.module.support.heartbeat.HeartBeatRecordHandler;
import net.lab1024.smartadmin.service.module.support.heartbeat.core.HeartBeatManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [ 服务心跳配置 ]
 *
 * @author 罗伊
 * @date 2021/1/9 14:13
 */
@Configuration
public class SmartHeartBeatConfig {

    /**
     * 延迟执行时间
     */
    @Value("${heart-beat.delayHandlerTime}")
    private Long delayHandlerTime;

    /**
     * 间隔执行时间
     */
    @Value("${heart-beat.intervalTime}")
    private Long intervalTime;

    @Autowired
    private HeartBeatRecordHandler heartBeatRecordHandler;


    @Bean
    public HeartBeatManager heartBeatManager(){
        return new HeartBeatManager(delayHandlerTime,intervalTime,heartBeatRecordHandler);
    }
}