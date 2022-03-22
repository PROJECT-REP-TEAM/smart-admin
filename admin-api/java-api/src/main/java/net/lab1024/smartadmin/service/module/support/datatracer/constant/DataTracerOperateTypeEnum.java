package net.lab1024.smartadmin.service.module.support.datatracer.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.smartadmin.service.common.enumeration.BaseEnum;

/**
 * [  ]
 *
 * @author 罗伊
 */
public interface DataTracerOperateTypeEnum extends BaseEnum {

    @AllArgsConstructor
    @Getter
    enum Common implements BaseEnum {
        SAVE(1, "保存"),
        UPDATE(2, "更新"),
        DELETE(3, "删除");

        private final Integer value;

        private final String desc;
    }

}
