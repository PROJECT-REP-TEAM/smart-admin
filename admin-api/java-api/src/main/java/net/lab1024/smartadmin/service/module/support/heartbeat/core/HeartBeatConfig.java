package net.lab1024.smartadmin.service.module.support.heartbeat.core;

import lombok.Builder;
import lombok.Data;

/**
 * [  ]
 *
 * @author 罗伊
 *
 */
@Data
@Builder
public class HeartBeatConfig {

    /**
     * 延迟执行时间
     */
    private Long delayHandlerTime;

    /**
     * 间隔执行时间
     */
    private Long intervalTime;
}
