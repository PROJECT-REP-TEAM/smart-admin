package net.lab1024.smartadmin.service.config;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.module.support.reload.SmartReloadCommand;
import net.lab1024.smartadmin.service.module.support.reload.core.SmartReloadManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [  ]
 *
 * @author zhuoda
 * @date 2021/9/1 21:40
 */
@Slf4j
@Configuration
public class SmartReloadConfig {

    @Autowired
    private SmartReloadCommand smartReloadCommand;

    @Bean
    public SmartReloadManager initSmartReloadManager() {
        /**
         * 创建 Reload Manager 调度器
         */
        SmartReloadManager smartReloadManager = new SmartReloadManager(smartReloadCommand);
        return smartReloadManager;
    }
}
