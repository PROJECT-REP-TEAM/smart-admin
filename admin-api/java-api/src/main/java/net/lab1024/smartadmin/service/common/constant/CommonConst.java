package net.lab1024.smartadmin.service.common.constant;

import com.google.common.collect.ImmutableSet;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/9/14 15:48
 */
public class CommonConst {

    /**
     * 全局通用分隔符
     */
    public static final String SEPARATOR = ",";

    /**
     * 全局通用分隔符 逗号
     */
    public static final Character SEPARATOR_CHAR = ',';

    /**
     * 全局通用分隔符 斜杠
     */
    public static final String SEPARATOR_SLASH = "/";

    /**
     * 全局通用分隔符 下划线
     */
    public static final String UNDERLINE = "_";

    /**
     * 空字符串
     */
    public static final String EMPTY_STR = "";

    /**
     * 空 MaP
     * 注意：放入元素会抛异常
     */
    public static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap<>(0));

    /**
     * 空 list
     * 注意：放入元素会抛异常
     */
    public static final List EMPTY_LIST = Collections.unmodifiableList(new ArrayList<>(0));

    /**
     * 空字符串
     */
    public static final long DEFAULT_PARENT_ID = 0L;

    /**
     * 空字符串
     */
    public static final int ZERO = 0;


    public static final class Token {

        public static final String DEFAULT_TOKEN = "default-token-";

        public static final String INNER_TOKEN_NAME = "inner-token";

        public static final String OUTER_TOKEN_NAME = "x-access-token";

    }

    public static final class FileFolderConst {

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

    }

    public static final class System {
        /**
         * oss url redis 过期时间
         */
        public static final int FILE_URL_EXPIRE_SECOND = 3600;

        public static final int FILE_VO_EXPIRE_SECOND = 86400;

    }

    /**
     * 长度类常量
     */
    public static final class NumberLimit {
        /**
         * 文件名称长度
         */
        public static final int FILE_NAME = 100;

    }

    public static final class ApiUrl {
        /**
         * 基础 api 前缀
         */
        public static final String API_PREFIX_SUPPORT = "/support";

        /**
         * 后管 api 前缀
         */
        public static final String API_PREFIX_ADMIN = "/admin";

    }

    public static final class Password {

        public static final String DEFAULT = "123456";

        public static final String SALT_FORMAT = "smart_%s_admin";
    }

    public static final class CommonCollection {

        public static final Set<String> IGNORE_URL = ImmutableSet.of("/swagger", "Excel", "/h5/api");

        public static Boolean contain(Set<String> ignores, String uri) {
            if (CollectionUtils.isEmpty(ignores)) {
                return false;
            }
            for (String ignoreUrl : ignores) {
                if (uri.startsWith(ignoreUrl)) {
                    return true;
                }
            }
            return false;
        }
    }
}
