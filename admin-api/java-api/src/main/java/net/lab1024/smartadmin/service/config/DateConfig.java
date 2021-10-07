package net.lab1024.smartadmin.service.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import net.lab1024.smartadmin.service.common.util.date.SmartDateFormatterEnum;
import net.lab1024.smartadmin.service.common.util.date.SmartLocalDateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * java8 localDate 时间类格式化配置
 *
 * @author listen
 * @date 2019年10月18日 19:02:55
 */
@Configuration
public class DateConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.deserializers(new LocalDateDeserializer(SmartDateFormatterEnum.YMD.getFormatter()));
            builder.deserializers(new LocalDateTimeDeserializer(SmartDateFormatterEnum.YMD_HMS.getFormatter()));
            builder.serializers(new LocalDateSerializer(SmartDateFormatterEnum.YMD.getFormatter()));
            builder.serializers(new LocalDateTimeSerializer(SmartDateFormatterEnum.YMD_HMS.getFormatter()));
        };
    }

    /**
     * string 转为 LocalDate 配置类
     *
     * @author Turbolisten
     * @date 2020/3/6 14:34
     */
    @Configuration
    public static class SmartConverterStringToLocalDateTime implements Converter<String, LocalDateTime> {

        @Override
        public LocalDateTime convert(String str) {
            if (StringUtils.isBlank(str)) {
                return null;
            }
            LocalDateTime localDateTime;
            try {
                localDateTime = SmartLocalDateUtil.parse(str, SmartDateFormatterEnum.YMD_HMS);
            } catch (DateTimeParseException e) {
                throw new RuntimeException("请输入正确的日期格式：yyyy-MM-dd HH:mm:ss");
            }
            return localDateTime;
        }


    }

    /**
     * string 转为 LocalDate 配置类
     *
     * @author Turbolisten
     * @date 2020/3/6 14:34
     */
    @Configuration
    public static class SmartConverterStringToLocalDate implements Converter<String, LocalDate> {

        @Override
        public LocalDate convert(String str) {
            if (StringUtils.isBlank(str)) {
                return null;
            }
            LocalDate localDate;
            try {
                localDate = SmartLocalDateUtil.parseDate(str, SmartDateFormatterEnum.YMD);
            } catch (DateTimeParseException e) {
                throw new RuntimeException("请输入正确的日期格式：yyyy-MM-dd");
            }
            return localDate;
        }
    }
}