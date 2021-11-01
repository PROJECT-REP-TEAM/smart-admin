package net.lab1024.smartadmin.service.constant;

/**
 * redis key 常量类
 *
 * @author zhuoda
 */
public class RedisKeyConst {

    public static final String PROJECT = "sa:";

    public class Support {

        public static final String FILE_URL = PROJECT + "file:";

        public static final String FILE_VO = PROJECT + "file-vo:";

        public static final String LOCK = PROJECT + "lock:";

        public static final String ID_GENERATOR = LOCK + "id-generator:";

        // 验证码
        public static final String CAPTCHA = PROJECT + "captcha:";

    }

}
