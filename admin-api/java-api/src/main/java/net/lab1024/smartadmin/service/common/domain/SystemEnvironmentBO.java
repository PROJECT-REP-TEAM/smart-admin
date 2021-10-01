package net.lab1024.smartadmin.service.common.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.smartadmin.service.common.enumconst.SystemEnvironmentEnum;

/**
 * 系统环境
 *
 * @author zhuoda
 * @Date 2021/8/13
 */
@AllArgsConstructor
@Getter
public class SystemEnvironmentBO {

    /**
     * 是否位生产环境
     */
    private Boolean isProd;

    /**
     * 当前环境
     */
    private SystemEnvironmentEnum currentEnvironment;
}
