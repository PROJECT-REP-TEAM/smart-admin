package net.lab1024.smartadmin.service.module.support.systemconfig;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.smartadmin.service.common.constant.BaseEnum;

/**
 * [ 系统配置常量类 ]
 *
 * @author 罗伊
 * @version 1.0
 * @date
 * @since JDK1.8
 */
@Getter
@AllArgsConstructor
public enum SystemConfigKeyEnum implements BaseEnum {

    /**
     * 超管id
     */
    EMPLOYEE_SUPERMAN("employee_superman", "超管id"),

    /**
     * 本地上传路径前缀
     */
    LOCAL_UPLOAD_URL_PREFIX("local_upload_url_prefix", "本地上传路径前缀"),

    ;

    private final String value;

    private final String desc;
}
