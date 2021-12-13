package net.lab1024.smartadmin.service.胡克er;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.common.code.ErrorCodeRegister;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 应用启动加载
 *
 * @author zhuo
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@Component
public class SmartAdminStartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {

        log.info("###################### SmartAdmin v2 init start ######################");

        // 初始化状态码
        ErrorCodeRegister.init();

        log.info("###################### SmartAdmin v2 init complete ######################");
    }
}