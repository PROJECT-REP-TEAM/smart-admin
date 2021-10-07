package net.lab1024.smartadmin.service.common.util;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 *
 * @author Administrator
 */
public class SmartCollectionUtil {

    public static <K, V> boolean isEmptyMap(Map<K, V> map) {
        if (map == null || map.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 分批处理数据
     *
     * @param list  集合
     * @param func  执行方法
     * @param limit 每次执行数量
     * @author listen
     */
    public static <T> void batchExecute(List<T> list, Function<List<T>, Object> func, int limit) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<T> tempList = new ArrayList<>(limit);
        for (T e : list) {
            tempList.add(e);
            if (tempList.size() >= limit) {
                func.apply(tempList);
                tempList.clear();
            }
        }
        if (CollectionUtils.isNotEmpty(tempList)) {
            func.apply(tempList);
        }
    }

    /**
     * 分批处理数据 支持两个参数
     *
     * @param list  集合
     * @param func  执行方法
     * @param limit 每次执行数量
     * @author listen
     */
    public static <T, R> void batchExecute(List<T> list, R obj, BiFunction<List<T>, R, Object> func, int limit) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        limit = 0 >= limit ? 50 : limit;
        List<T> tempList = new ArrayList<>(limit);
        for (T e : list) {
            tempList.add(e);
            if (tempList.size() >= limit) {
                func.apply(tempList, obj);
                tempList.clear();
            }
        }
        if (CollectionUtils.isNotEmpty(tempList)) {
            func.apply(tempList, obj);
        }
    }

    public static <K, V> boolean isNotEmptyMap(Map<K, V> map) {
        return !isEmptyMap(map);
    }
}
