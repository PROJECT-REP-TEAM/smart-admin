package net.lab1024.smartadmin.module.support.operatelog.core;

import lombok.Builder;
import lombok.Data;
import net.lab1024.smartadmin.module.support.operatelog.domain.OperateLogEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;
import java.util.function.Supplier;

/**
* @Description:    配置
* @Author:         卓大
* @CreateDate:     2021/8/3 21:54
* @Version:        1.0
*/
@Data
@Builder
public class OperateLogConfig {

    /**
     * 用户信息
     */
    private Function<HttpServletRequest, OperateLogUser> userFunction;
    /**
     * 是否启用
     */
    private Supplier<Boolean> openSupplier;

    /**
     * 过滤器
     */
    private Function<String, Boolean> filterFunction;

    /**
     * 操作日志存储方法
     */
    private Function<OperateLogEntity, Boolean> saveFunction;

    /**
     * 核心线程数
     */
    private Integer corePoolSize;

    /**
     * 队列大小
     */
    private Integer queueCapacity;


}
