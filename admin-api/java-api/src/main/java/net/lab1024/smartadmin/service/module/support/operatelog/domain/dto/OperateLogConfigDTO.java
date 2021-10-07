package net.lab1024.smartadmin.service.module.support.operatelog.domain.dto;

import lombok.Builder;
import lombok.Data;
import net.lab1024.smartadmin.service.module.support.operatelog.domain.OperateLogEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;
import java.util.function.Supplier;

/**
* @Description:    日志配置DTO
* @Author:         sbq
* @CreateDate:     2019/8/3 8:54
* @Version:        1.0
*/
@Data
@Builder
public class OperateLogConfigDTO {

    /**
     * 用户信息
     */
    private Function<HttpServletRequest, OperateLogUserDTO> userFunction;
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
