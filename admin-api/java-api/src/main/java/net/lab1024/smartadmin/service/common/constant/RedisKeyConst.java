package net.lab1024.smartadmin.service.common.constant;

/**
 * redis key 常量类
 *
 * @author listen
 * @date 2019/09/23 20:48
 */
public class RedisKeyConst {

    public class Base {

        private static final String PROJECT = "base:";

        public static final String FILE_URL = PROJECT + "file:";

        public static final String FILE_VO = PROJECT + "fileVO:";

        public static final String LOCK = PROJECT + "lock:";

        public static final String ID_GENERATOR = LOCK + "id:";

        public static final String CAPTCHA = PROJECT + "captcha:";

    }

}
