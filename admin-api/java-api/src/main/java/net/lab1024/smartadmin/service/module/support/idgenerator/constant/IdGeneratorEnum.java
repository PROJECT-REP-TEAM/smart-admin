package net.lab1024.smartadmin.service.module.support.idgenerator.constant;

import net.lab1024.smartadmin.service.common.enumconst.BaseEnum;

/**
 * @author Administrator
 */

public enum IdGeneratorEnum implements BaseEnum {


    ;

    private Integer id;

    private String keyName;

    IdGeneratorEnum(int id, String keyName) {
        this.id = id;
        this.keyName = keyName;
    }

    @Override
    public String toString() {
        return "IdGeneratorEnum{" + "id=" + id + ", keyName='" + keyName + '\'' + '}';
    }

    public int getId() {
        return id;
    }

    public String getKeyName() {
        return keyName;
    }

    @Override
    public Integer getValue() {
        return id;
    }

    @Override
    public String getDesc() {
        return null;
    }
}
