package net.lab1024.smartadmin.service.module.support.idgenerator.service;

import net.lab1024.smartadmin.service.module.support.idgenerator.constant.IdGeneratorEnum;

import java.util.List;

/**
 * @author zhuoda
 * @Date 2021-11-10
 */
public interface IdGeneratorService {

    /**
     * 生成
     *
     * @param idGeneratorEnum
     * @return
     */
    String generate(IdGeneratorEnum idGeneratorEnum);


    /**
     * 生成n个
     *
     * @param idGeneratorEnum
     * @param count
     * @return
     */
    List<String> generate(IdGeneratorEnum idGeneratorEnum, int count);

}
