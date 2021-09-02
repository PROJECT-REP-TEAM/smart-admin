package net.lab1024.smartadmin.service.config;

import net.lab1024.smartadmin.service.util.date.SmartDateFormatterEnum;
import net.lab1024.smartadmin.service.util.date.SmartLocalDateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * string 转为 LocalDate 配置类
 *
 * @author 胡克
 * @date 2020/3/6 14:34
 */
@Configuration
public class SmartConverterStringToLocalDateTime implements Converter<String, LocalDateTime> {


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
