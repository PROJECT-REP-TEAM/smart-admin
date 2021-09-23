package net.lab1024.smartadmin.service.common.constant;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * [ 通用常量 ]
 *
 * @author zhuoda
 */
public class CommonConst {

    public static final int ZERO = 0;

    public static final long ONE = 1;

    /**
     * 全局通用分隔符
     */
    public static final String SEPARATOR = ",";

    /**
     * 全局通用分隔符 下划线
     */
    public static final String UNDERLINE = "_";

    /**
     * 全局通用分隔符
     */
    public static final Character SEPARATOR_CHAR = ',';

    /**
     * 全局通用分隔符 斜杠
     */
    public static final String SEPARATOR_SLASH = "/";

    /**
     * 空字符串
     */
    public static final String EMPTY_STR = "";

    /**
     * 空MaP
     */
    public static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap<>(0));

    /**
     * 空 list
     */
    public static final List EMPTY_LIST = Collections.unmodifiableList(new ArrayList<>(0));

    /**
     * 全局通用 默认id
     */
    public static final long DEFAULT_ID = 0L;

    /**
     * 100 常量
     */
    public static final Integer ONE_HUNDRED = 100;

    /**
     * 100 常量
     */
    public static final BigDecimal ONE_HUNDRED_DECIMAL = new BigDecimal(100);

    /**
     * 全局通用 默认父级id 最顶级
     */
    public static final Long DEFAULT_PARENT_ID = 0L;

    public static final Interner<String> STRING_POOL = Interners.newWeakInterner();

    public static final class ApiUrl {
        /**
         * 基础 api 前缀
         */
        public static final String API_PREFIX_SUPPORT = "/support";

        /**
         * 后管 api 前缀
         */
        public static final String API_PREFIX_ADMIN = "/admin";

        /**
         * app 业务 api 前缀
         */
        public static final String API_PREFIX_APP = "/app";

        /**
         * PC 业务 api 前缀
         */
        public static final String API_PREFIX_PC = "/pc";

        /**
         * 开放平台 api 前缀
         */
        public static final String API_PREFIX_OPEN = "/open";

    }

    public static final String IGNORE_H5_URL_MAPPING = "/h5/api";

    public static final class FileServiceConst {

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
        public static final int FILE_URL_EXPIRE_SECOND = 7100;

        public static final int FILE_VO_EXPIRE_SECOND = 86400;

        /**
         * 系统 id
         */
        public static final Long SYSTEM_ID = 0L;

        /**
         * 系统 名称
         */
        public static final String SYSTEM_NAME = "小蜜蜂CRM系统";
    }

    /**
     * 长度类常量
     */
    public static final class NumberLimit {

        /**
         * 用户昵称
         */
        public static final int USER_NICKNAME = 15;

        /**
         * 短信验证码长度
         */
        public static final int SMS_CODE = 6;

        /**
         * 短信发送间隔 秒
         */
        public static final int SMS_SEND_INTERVAL = 60;

        /**
         * 短信有效期 秒
         */
        public static final int SMS_VALID_SECOND = 300;

        /**
         * 验证码短信 每天的限制
         */
        public static final int SMS_DAY_LIMIT = 10;

        /**
         * 文件名称长度
         */
        public static final int FILE_NAME = 100;

    }

    public static final class Password {

        public static final String DEFAULT = "123456";

        public static final String SALT_FORMAT = "xiaomifeng_%s_crm";
    }

    public static final class CommonCollection {

        public static final Set<String> IGNORE_URL = ImmutableSet.of("/swagger", "Excel");

        public static final Set<String> IGNORE_URL_MAPPING = ImmutableSet.of(IGNORE_H5_URL_MAPPING);

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

    /**
     * 用户类型
     */
    public static class UserType {

        /**
         * 用户
         */
        public static final Integer USER = 1;

        /**
         * 管理端
         */
        public static final Integer ADMIN = 3;

        /**
         * 系统
         */
        public static final Integer SYSTEM = 4;

        /**
         * 游客
         */
        public static final Integer NO_LOGIN = 5;
    }

    /**
     * 请求头常量
     */
    public static class RequestHeader {

        public static final String TOKEN = "x-access-token";

        public static final String USER_AGENT = "user-agent";

        /**
         * 用户唯一标识
         */
        public static final String USER_IDENTITY = "user-identity";

        /**
         * 经纬度
         */
        public static final String USER_GEO = "user-geo";

        /**
         * 位置
         */
        public static final String USER_LOCATION = "user-location";

    }

    /**
     * 系统类型
     */
    public static class SystemType {

        public static final int XMF_CRM = 100;

        public static final int REN_MIN = 200;

        public static final int ZHI_KAO = 300;

        public static final int XIAO_YING_YI = 400;

    }

    /**
     * 支付方式
     */
    public static class PayType {

        public static final int WX = 10;

        public static final int ZFB = 15;

        /**
         * 20 线下支付
         */
        public static final int OFFLINE = 20;

        /**
         * 21 银行卡
         */
        public static final int BANK = 21;

    }

    public static final class Token {

        public static final String DEFAULT_TOKEN = "default-token-id-";

        public static final String INNER_TOKEN_NAME = "inner-token";

        public static final String OUTER_TOKEN_NAME = "x-access-token";
    }
}
