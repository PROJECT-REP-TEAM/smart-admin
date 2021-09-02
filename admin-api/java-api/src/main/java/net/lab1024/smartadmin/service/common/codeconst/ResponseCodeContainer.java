package net.lab1024.smartadmin.service.common.codeconst;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2021/1/8 11:28
 */
@Slf4j
public class ResponseCodeContainer {

    private static final Map<Integer, ResponseCodeConst> RESPONSE_CODE_MAP = new HashMap<>();

    private static final Map<Class<? extends ResponseCodeConst>, int[]> RESPONSE_CODE_RANGE_MAP = new HashMap<>();

    /**
     * id的范围：[start, end]左闭右闭
     *
     * @param clazz
     * @param start
     * @param end
     */
    public static void register(Class<? extends ResponseCodeConst> clazz, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException("<ResponseDTO> start > end!");
        }

        if (RESPONSE_CODE_RANGE_MAP.containsKey(clazz)) {
            throw new IllegalArgumentException(String.format("<ResponseDTO> Class:%s already exist!", clazz.getSimpleName()));
        }
        RESPONSE_CODE_RANGE_MAP.forEach((k, v) -> {
            if ((start >= v[0] && start <= v[1]) || (end >= v[0] && end <= v[1])) {
                throw new IllegalArgumentException(String.format("<ResponseDTO> Class:%s 's id range[%d,%d] has " + "intersection with " + "class:%s", clazz.getSimpleName(), start, end,
                        k.getSimpleName()));
            }
        });

        RESPONSE_CODE_RANGE_MAP.put(clazz, new int[]{start, end});

        // 提前初始化static变量，进行范围检测
        Field[] fields = clazz.getFields();
        if (fields.length != 0) {
            try {
                fields[0].get(clazz);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                log.error("", e);
            }
        }
    }

    public static void put(ResponseCodeConst codeConst) {
        int[] idRange = RESPONSE_CODE_RANGE_MAP.get(codeConst.getClass());
        if (idRange == null) {
            throw new IllegalArgumentException(String.format("<ResponseDTO> Class:%s has not been registered!", codeConst.getClass().getSimpleName()));
        }
        int code = codeConst.code;
        if (code < idRange[0] || code > idRange[1]) {
            throw new IllegalArgumentException(String.format("<ResponseDTO> Id(%d) out of range[%d,%d], " + "class:%s", code, idRange[0], idRange[1], codeConst.getClass().getSimpleName()));
        }
        if (RESPONSE_CODE_MAP.keySet().contains(code)) {
            log.error(String.format("<ResponseDTO> Id(%d) out of range[%d,%d], " + "class:%s  code is repeat!", code, idRange[0], idRange[1], codeConst.getClass().getSimpleName()));
            System.exit(0);
        }
        RESPONSE_CODE_MAP.put(code, codeConst);
    }

}
