package net.lab1024.smartadmin.service.module.support.reload.core;


import net.lab1024.smartadmin.service.module.support.reload.core.anno.SmartReload;
import net.lab1024.smartadmin.service.module.support.reload.core.domain.ReloadObject;
import net.lab1024.smartadmin.service.module.support.reload.core.thread.SmartReloadScheduler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SmartReloadManager 管理器
 * <p>
 * 可以在此类中添加 检测任务 以及注册 处理程序
 *
 * @author zhuoda
 */
public class SmartReloadManager implements BeanPostProcessor {

    private Map<String, ReloadObject> reloadObjectMap = new ConcurrentHashMap<>();

    private SmartReloadScheduler reloadScheduler;

    private SmartReloadLogger logger;


    public SmartReloadManager(SmartReloadLogger logger,
                              AbstractSmartReloadCommand reloadCommand,
                              int threadCount) {

        if (logger == null) {
            throw new ExceptionInInitializerError("SmartReloadLoggerImp cannot be null");
        }
        if (threadCount < 1) {
            throw new ExceptionInInitializerError("threadCount must be greater than 1");
        }
        this.logger = logger;
        this.reloadScheduler = new SmartReloadScheduler(this.logger, threadCount);
        this.reloadScheduler.addCommand(reloadCommand);
        reloadCommand.setReloadManager(this);
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        if (methods == null) {
            return bean;
        }
        for (Method method : methods) {
            SmartReload smartReload = method.getAnnotation(SmartReload.class);
            if (smartReload == null) {
                continue;
            }
            int paramCount = method.getParameterCount();
            if (paramCount > 1) {
                logger.error("<<SmartReloadManager>> register tag reload : " + smartReload.value() + " , param count cannot greater than one !");
                continue;
            }
            String reloadTag = smartReload.value();
            this.register(reloadTag, new ReloadObject(bean, method));
        }
        return bean;
    }

    /**
     * 注册reload
     *
     * @param tag
     * @param reloadObject
     */
    private void register(String tag, ReloadObject reloadObject) {
        if (reloadObjectMap.containsKey(tag)) {
            logger.error("<<SmartReloadManager>> register duplicated tag reload : " + tag + " , and it will be cover!");
        }
        reloadObjectMap.put(tag, reloadObject);
    }

    /**
     * 获取重载对象
     * @return
     */
    public Map<String, ReloadObject> reloadObjectMap() {
        return this.reloadObjectMap;
    }


}
