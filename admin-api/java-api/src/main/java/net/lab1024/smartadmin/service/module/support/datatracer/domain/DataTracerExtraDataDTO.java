package net.lab1024.smartadmin.service.module.support.datatracer.domain;

import lombok.Data;

/**
 * [  ]
 *
 * @author 罗伊
 * @date 2020/8/25 17:23
 */
@Data
public class DataTracerExtraDataDTO {

    /**
     * 对象所属类
     */
    private Class clazz;

    /**
     * 原对象
     */
    private Object originObj;

    /**
     * 新对象
     */
    private Object newObj;

    public DataTracerExtraDataDTO(Class clazz, Object originObj, Object newObj) {
        this.clazz = clazz;
        this.originObj = originObj;
        this.newObj = newObj;
    }
}
