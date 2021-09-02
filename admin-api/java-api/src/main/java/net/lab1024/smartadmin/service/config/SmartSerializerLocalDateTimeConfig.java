package net.lab1024.smartadmin.service.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import net.lab1024.smartadmin.service.util.date.SmartDateFormatterEnum;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * java8 localDate 时间类格式化配置
 *
 * @author listen
 * @date 2021年8月31日 21:19
 */
@Configuration
public class SmartSerializerLocalDateTimeConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.deserializers(new LocalDateDeserializer(SmartDateFormatterEnum.YMD.getFormatter()));
            builder.deserializers(new LocalDateTimeDeserializer(SmartDateFormatterEnum.YMD_HMS.getFormatter()));
            builder.serializers(new LocalDateSerializer(SmartDateFormatterEnum.YMD.getFormatter()));
            builder.serializers(new LocalDateTimeSerializer(SmartDateFormatterEnum.YMD_HMS.getFormatter()));
        };
    }

}