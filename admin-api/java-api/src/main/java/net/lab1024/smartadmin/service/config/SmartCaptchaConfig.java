package net.lab1024.smartadmin.service.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import net.lab1024.smartadmin.service.module.support.captcha.render.CaptchaNoise;
import net.lab1024.smartadmin.service.module.support.captcha.render.CaptchaWordRenderer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * [ 验证码配置 ]
 *
 * @author 罗伊
 * @version 1.0
 * @since JDK1.8
 */
@Configuration
public class SmartCaptchaConfig {

    /**
     * 图形验证码配置
     *
     * @return
     */
    @Bean
    public DefaultKaptcha getDefaultCaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no" );
        properties.setProperty("kaptcha.border.color", "34,114,200" );
        properties.setProperty("kaptcha.image.width", "125" );
        properties.setProperty("kaptcha.image.height", "45" );
        properties.setProperty("kaptcha.textproducer.char.string", "123456789" );
        properties.setProperty("kaptcha.textproducer.char.length", "4" );
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Arial Narrow,Serif,Helvetica,Tahoma,Times New Roman,Verdana" );
        properties.setProperty("kaptcha.textproducer.font.size", "38" );

        properties.setProperty("kaptcha.background.clear.from", "white" );
        properties.setProperty("kaptcha.background.clear.to", "white" );

        properties.setProperty("kaptcha.word.impl", CaptchaWordRenderer.class.getName());
        properties.setProperty("kaptcha.noise.impl", CaptchaNoise.class.getName());

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

}
