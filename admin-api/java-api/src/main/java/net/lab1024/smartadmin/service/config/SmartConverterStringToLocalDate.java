package net.lab1024.smartadmin.service.config;

import net.lab1024.smartadmin.service.util.date.SmartDateFormatterEnum;
import net.lab1024.smartadmin.service.util.date.SmartLocalDateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * string 转为 LocalDate 配置类
 *
 * @author 胡克
 * @date 2020/3/6 14:34
 */
@Configuration
public class SmartConverterStringToLocalDate implements Converter<String, LocalDate> {

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
