package net.lab1024.smartadmin.service.module.support.datatracer.constant;


import net.lab1024.smartadmin.service.common.enumconst.BaseEnum;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/25 17:04
 */
public interface DataTracerOperateTypeEnum extends BaseEnum {

    Integer INIT_CODE = 4;

    enum Common implements BaseEnum {
        SAVE(0, "保存"),
        SAVE_SUBMIT(1, "保存并提交"),
        UPDATE(2, "更新"),
        UPDATE_SUBMIT(3, "更新并提交"),
        DELETE(4, "删除");

        private Integer value;
        private String desc;

        Common(Integer value, String desc) {
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

}
