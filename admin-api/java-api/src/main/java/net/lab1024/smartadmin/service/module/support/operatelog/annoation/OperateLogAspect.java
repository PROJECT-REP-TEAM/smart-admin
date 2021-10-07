package net.lab1024.smartadmin.service.module.support.operatelog.annoation;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.smartadmin.service.module.support.operatelog.OperateLogDao;
import net.lab1024.smartadmin.service.module.support.operatelog.domain.dto.OperateLogConfigDTO;
import net.lab1024.smartadmin.service.module.support.operatelog.domain.dto.OperateLogUserDTO;
import net.lab1024.smartadmin.service.module.support.operatelog.domain.OperateLogEntity;
import net.lab1024.smartadmin.service.third.SmartApplicationContext;
import net.lab1024.smartadmin.service.common.util.SmartStringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * [  操作日志记录处理,对所有OperateLog注解的Controller进行操作日志监控 ]
 *
 * @author 罗伊
 */
@Slf4j
@Aspect
public class OperateLogAspect {

    private static final String pointCut = "@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)";

    private OperateLogConfigDTO smartLogConfig;

    /**
     * 线程池
     */
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 构造方法
     */
    public OperateLogAspect(OperateLogConfigDTO operateLogConfigDTO) {
        smartLogConfig = operateLogConfigDTO;
        this.initThread(smartLogConfig);
    }

    @Pointcut(pointCut)
    public void logPointCut() {
    }

    @AfterReturning(pointcut = "logPointCut()")
    public void doAfterReturning(JoinPoint joinPoint) {
        handleLog(joinPoint, null);
    }

    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e);
    }

    /**
     * 初始化线程池
     */
    private void initThread(OperateLogConfigDTO configDTO) {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        if (null != configDTO.getCorePoolSize()) {
            corePoolSize = configDTO.getCorePoolSize();
        }
        taskExecutor = new ThreadPoolTaskExecutor();
        //线程初始化
        taskExecutor.initialize();
        // 设置核心线程数
        taskExecutor.setCorePoolSize(corePoolSize);
        // 设置最大线程数
        taskExecutor.setMaxPoolSize(corePoolSize * 2);
        // 设置队列容量
        taskExecutor.setQueueCapacity(1000);
        // 设置线程活跃时间（秒）
        taskExecutor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        taskExecutor.setThreadNamePrefix("smart-logs");
        // 设置拒绝策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e) {
        try {
            OperateLog operateLog = this.getAnnotationLog(joinPoint);
            if (operateLog == null) {
                return;
            }
            this.submitLog(joinPoint, e);
        } catch (Exception exp) {
            log.error("保存操作日志异常:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    private OperateLog getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        OperateLog classAnnotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), OperateLog.class);

        if (method != null) {
            return classAnnotation;
        }
        return null;
    }

    /**
     * swagger API
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private Api getApi(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Api classAnnotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), Api.class);

        if (method != null) {
            return classAnnotation;
        }
        return null;
    }

    /**
     * swagger ApiOperation
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private ApiOperation getApiOperation(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(ApiOperation.class);
        }
        return null;
    }

    /**
     * 提交存储操作日志
     *
     * @param joinPoint
     * @param e
     * @throws Exception
     */
    private void submitLog(final JoinPoint joinPoint, final Throwable e) throws Exception {
        Boolean isOpen = this.isOpen();
        if (!isOpen) {
            return;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Boolean filter = this.filterUrl(request.getRequestURI());
        if (filter) {
            return;
        }
        //设置用户信息
        OperateLogUserDTO user = smartLogConfig.getUserFunction().apply(request);
        if (user == null) {
            return;
        }
        // 设置方法名称
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        String operateMethod = className + "." + methodName;
        Object[] args = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();
        for (Object obj : args) {
            sb.append(obj.getClass().getSimpleName());
            sb.append("[");
            sb.append(JSON.toJSONString(obj));
            sb.append("]");
        }
        String params = sb.toString();
        String failReason = null;
        Boolean successFlag = true;
        if (e != null) {
            successFlag = false;
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            failReason = sw.toString();
            pw.flush();
            pw.close();
            sw.flush();
            sw.close();
        }
        OperateLogEntity operateLogEntity =
                OperateLogEntity.builder()
                        .userId(user.getUserId())
                        .userName(user.getUserName())
                        .url(request.getRequestURI())
                        .method(operateMethod)
                        .param(params)
                        .failReason(failReason)
                        .successFlag(successFlag).build();
        ApiOperation apiOperation = this.getApiOperation(joinPoint);
        if (apiOperation != null) {
            operateLogEntity.setContent(apiOperation.value());
        }
        Api api = this.getApi(joinPoint);
        if (api != null) {
            String[] tags = api.tags();
            operateLogEntity.setModule(SmartStringUtil.join(tags, ","));
        }
        taskExecutor.execute(() -> {
            this.saveLog(operateLogEntity);
        });
    }

    /**
     * 是否开启操作日志
     *
     * @return
     */
    private Boolean isOpen() {
        if (smartLogConfig.getOpenSupplier() == null) {
            return Boolean.TRUE;
        }
        return smartLogConfig.getOpenSupplier().get();
    }

    /**
     * 需要过滤的url
     *
     * @param url
     * @return
     */
    private Boolean filterUrl(String url) {
        if (smartLogConfig.getFilterFunction() == null) {
            return Boolean.FALSE;
        }
        return smartLogConfig.getFilterFunction().apply(url);
    }

    /**
     * 保存操作日志
     * @param operateLogEntity
     * @return
     */
    private Boolean saveLog(OperateLogEntity operateLogEntity) {
        if (smartLogConfig.getSaveFunction() == null) {
            BaseMapper mapper = SmartApplicationContext.getBean(OperateLogDao.class);
            if (mapper == null) {
                return false;
            }
            mapper.insert(operateLogEntity);
            return true;
        }
        return smartLogConfig.getSaveFunction().apply(operateLogEntity);
    }

}
