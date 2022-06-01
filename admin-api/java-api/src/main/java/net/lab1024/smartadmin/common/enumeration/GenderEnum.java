package net.lab1024.smartadmin.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举类
 *
 * @author 胡克
 * @date 2019/09/24 16:50
 */
@AllArgsConstructor
@Getter
public enum GenderEnum implements BaseEnum {

    /**
     * 0 未知
     */
    UNKNOWN(0, "未知"),

    /**
     * 男 1 奇数为阳
     */
    MAN(1, "男"),

    /**
     * 女 2 偶数为阴
     */
    WOMAN(2, "女");

    private final Integer value;

    private final String desc;
}
