package net.lab1024.smartadmin.service.common.constant;

/**
 * 性别枚举类
 *
 * @author listen
 * @date 2019/09/24 16:50
 */
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

    private final Integer gender;

    private final String desc;

    GenderEnum(Integer gender, String desc) {
        this.gender = gender;
        this.desc = desc;
    }
    /**
     * 获取枚举类的值
     *
     * @return Integer
     */
    @Override
    public Integer getValue() {
        return gender;
    }
    /**
     * 获取枚举类的说明
     *
     * @return String
     */
    @Override
    public String getDesc() {
        return desc;
    }
}
