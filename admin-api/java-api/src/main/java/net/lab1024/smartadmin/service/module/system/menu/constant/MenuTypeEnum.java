package net.lab1024.smartadmin.service.module.system.menu.constant;


import net.lab1024.smartadmin.service.common.enumeration.BaseEnum;

/**
 * 菜单类型枚举
 *
 * @author lihaifan
 * @date 2021/7/29 15:30
 */
public enum MenuTypeEnum implements BaseEnum {
    /**
     * 目录
     */
    CATALOG(1, "目录"),
    /**
     * 菜单
     */
    MENU(2, "菜单"),
    /**
     * 功能点
     */
    POINTS(3, "功能点");

    private Integer value;

    private String desc;


    MenuTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
