package net.lab1024.smartadmin.service.common.constant;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

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

    /**
     * 空MaP
     */
    public static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap<>(0));

    /**
     * 空 list
     */
    public static final List EMPTY_LIST = Collections.unmodifiableList(new ArrayList<>(0));

    public static final Interner<String> STRING_POOL = Interners.newWeakInterner();


}
