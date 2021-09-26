package net.lab1024.smartadmin.service.module.support.file.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.smartadmin.service.common.enumconst.BaseEnum;

/**
 * 文件服务 文件夹位置类型枚举类
 *
 * @author listen
 * @date 2019年10月11日 15:34:47
 */
@AllArgsConstructor
@Getter
public enum FileFolderTypeEnum implements BaseEnum {

    COMMON(1, FileFolderTypeEnum.FOLDER_PUBLIC + "/common/", "通用"),

    ;

    /**
     * 公用读取文件夹 public
     */
    public static final String FOLDER_PUBLIC = "pu";

    /**
     * 私有读取文件夹 private
     */
    public static final String FOLDER_PRIVATE = "pr";

    /**
     * 文件夹格式
     */
    public static final String FOLDER_FORMAT = "folder";

    public static final String INFO = "1:通用";

    private final Integer value;

    private final String folder;

    private final String desc;
}

