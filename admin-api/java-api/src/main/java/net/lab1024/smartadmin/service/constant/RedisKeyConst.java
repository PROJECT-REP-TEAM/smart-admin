package net.lab1024.smartadmin.service.constant;

/**
 * redis key 常量类
 *
 * @author zhuoda
 */
public class RedisKeyConst {

    public static final String SEPARATOR = ":";

    private static final String PROJECT = "sa:";


    public class Support {

        public static final String FILE_URL = PROJECT + "file:";

        public static final String FILE_VO = PROJECT + "file-vo:";

        public static final String LOCK = PROJECT + "lock:";

        public static final String SERIAL_NUMBER = LOCK + "serial-number:";

        public static final String SERIAL_NUMBER_LAST_INFO = LOCK + "serial-number:last-info";

        // 验证码
        public static final String CAPTCHA = PROJECT + "captcha:";

    }

    public class System {

        public static final String TOKEN = PROJECT + "token:";

        public static final String USER_TOKEN_DEVICE = PROJECT + "token:device:";
    }

}
