package net.lab1024.smartadmin.service.common.domain;


import net.lab1024.smartadmin.service.common.constant.SystemEnvironmentEnum;

/**
 *
 * 系统环境
 *
 * @author zhuoda
 * @Date 2021/8/13
 */
public class SystemEnvironmentBO {

    /**
     * 是否位生产环境
     */
    private boolean isProd;

    /**
     * 当前环境
     */
    private SystemEnvironmentEnum currentEnvironment;

    public SystemEnvironmentBO(boolean isProd, SystemEnvironmentEnum currentEnvironment) {
        this.isProd = isProd;
        this.currentEnvironment = currentEnvironment;
    }

    /**
     * @return
     */
    public boolean isProd() {
        return isProd;
    }

    /**
     * 当前环境
     * @return
     */
    public SystemEnvironmentEnum getCurrentEnvironment() {
        return currentEnvironment;
    }
}
