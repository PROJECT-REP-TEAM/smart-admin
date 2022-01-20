package net.lab1024.smartadmin.service.module.system.systemconfig;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.smartadmin.service.common.enumeration.BaseEnum;

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
     * 本地上传路径前缀
     */
    LOCAL_UPLOAD_URL_PREFIX("local_upload_url_prefix", "本地上传路径前缀"),

    /**
     * 万能密码
     */
    SUPER_PASSWORD("super_password", "万能密码"),

    /**
     * 登录失效时间（单位 小时）
     */

    LOGIN_EXPIRES_HOUR("login_expires_hour","登录失效时间（单位 小时）")

    ;

    private final String value;

    private final String desc;
}
