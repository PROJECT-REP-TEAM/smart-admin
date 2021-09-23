package net.lab1024.smartadmin.service.module.support.file.domain;

import net.lab1024.smartadmin.service.common.constant.BaseEnum;
import net.lab1024.smartadmin.service.common.constant.CommonConst;

/**
 * 文件服务 文件夹位置类型枚举类
 *
 * @author listen
 * @date 2019年10月11日 15:34:47
 */
public enum FileFolderTypeEnum implements BaseEnum {

    COMMON(1, CommonConst.FileServiceConst.FOLDER_PUBLIC + "/common/", "通用"),

    ;

    public static final String INFO = "201:erp货物";

    private Integer value;

    private String folder;

    private String desc;

    FileFolderTypeEnum(Integer value, String folder, String desc) {
        this.value = value;
        this.folder = folder;
        this.desc = desc;
    }

    public String getFolder() {
        return folder;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}

