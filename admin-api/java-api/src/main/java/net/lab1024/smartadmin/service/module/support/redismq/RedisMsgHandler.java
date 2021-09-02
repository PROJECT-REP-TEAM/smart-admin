package net.lab1024.smartadmin.service.module.support.redismq;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.third.SmartApplicationContext;
import net.lab1024.smartadmin.service.util.SmartBaseEnumUtil;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * redis 订阅消息处理类
 *
 * @author 胡克
 * @date 2019/12/25 15:02
 */
@Slf4j
public class RedisMsgHandler {

    public static final String METHOD_NAME = "receiveMessage";

    private Map<RedisMsgTypeEnum, Method> redisHandle = Maps.newConcurrentMap();

    public RedisMsgHandler(String scanPath) {
        this.redisMqHandleFunction(scanPath);
    }

    public void redisMqHandleFunction(String scanPath) {
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(scanPath).addScanners(new MethodAnnotationsScanner()));
        Set<Method> methods = reflections.getMethodsAnnotatedWith(RedisMqHandle.class);
        for (Method method : methods) {
            RedisMqHandle redisMqHandle = method.getAnnotation(RedisMqHandle.class);
            if (redisMqHandle != null) {
                redisHandle.put(redisMqHandle.value(), method);
            }
        }
    }

    public void receiveMessage(String jsonMsg) throws InvocationTargetException, IllegalAccessException {
        log.info("Redis订阅消息处理:接收到消息->{}", jsonMsg);
        RedisMsgDTO redisMsgDTO = JSONObject.parseObject(jsonMsg, RedisMsgDTO.class);
        if (redisMsgDTO.getMsgType() == null) {
            log.error("Redis消息暂未指定消息类型");
            return;
        }
        RedisMsgTypeEnum msgTypeEnum = SmartBaseEnumUtil.getEnumByValue(redisMsgDTO.getMsgType(), RedisMsgTypeEnum.class);
        if (msgTypeEnum == null) {
            log.error("Redis消息类型错误");
            return;
        }
        Method handleMethod = redisHandle.get(msgTypeEnum);
        if (handleMethod == null) {
            log.error("Redis消息类型：{},暂未找到对应的处理类", msgTypeEnum.getDesc());
            return;
        }
        if (handleMethod.getParameterCount() == 0) {
            log.error("Redis消息类型：{},处理方法：{},无入参信息", msgTypeEnum.getDesc(), handleMethod.getName());
            return;
        }
        Object object = SmartApplicationContext.getBean(handleMethod.getDeclaringClass());
        if (object == null) {
            log.error("Redis消息类型处理方法：{},对应的类：{}，无对应的bean", msgTypeEnum.getDesc(), handleMethod.getDeclaringClass());
            return;
        }
        handleMethod.invoke(object, redisMsgDTO.getJsonData());

    }

}
