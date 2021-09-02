package net.lab1024.smartadmin.service.module.support.systemconfig;

import net.lab1024.smartadmin.service.common.constant.BaseEnum;

/**
 * [ 系统配置常量类 ]
 *
 * @author 罗伊
 * @version 1.0
 * @date
 * @since JDK1.8
 */
public class SystemConfigConst {


    public enum Key implements BaseEnum {

        /**
         * 超管id
         */
        EMPLOYEE_SUPERMAN("employee_superman", "超管id"),

        /**
         * 本地上传路径前缀
         */
        LOCAL_UPLOAD_URL_PREFIX("local_upload_url_prefix", "本地上传路径前缀"),

        ;

        private final String val;

        private final String desc;

        Key(String val, String desc) {
            this.val = val;
            this.desc = desc;
        }

        @Override
        public String getValue() {
            return val;
        }

        @Override
        public String getDesc() {
            return desc;
        }
    }

    public enum Group implements BaseEnum {

        /**
         * system 默认系统
         */
        SYSTEM("system", "默认系统"),

        ;

        private final String val;

        private final String desc;

        Group(String val, String desc) {
            this.val = val;
            this.desc = desc;
        }

        @Override
        public String getValue() {
            return val;
        }

        @Override
        public String getDesc() {
            return desc;
        }
    }

}
