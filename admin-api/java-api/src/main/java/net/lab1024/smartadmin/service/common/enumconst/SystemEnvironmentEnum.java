package net.lab1024.smartadmin.service.common.enumconst;


/**
 * 系统环境枚举类
 *
 * @author zhuoda
 */
public enum SystemEnvironmentEnum implements BaseEnum {
    /**
     * dev
     */
    DEV(SystemEnvironmentNameConst.DEV, "开发环境"),

    /**
     * sit
     */
    SIT(SystemEnvironmentNameConst.SIT, "测试环境"),

    /**
     * pre
     */
    PRE(SystemEnvironmentNameConst.PRE, "预发布环境"),

    /**
     * prod
     */
    PROD(SystemEnvironmentNameConst.PROD, "生产环境");

    private String value;

    private String desc;

    SystemEnvironmentEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 获取定义枚举value值
     *
     * @return Integer
     */
    @Override
    public String getValue() {
        return value;
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

    public static final class SystemEnvironmentNameConst {
        public static final String DEV = "dev";
        public static final String SIT = "sit";
        public static final String PRE = "pre";
        public static final String PROD = "prod";
    }

}
