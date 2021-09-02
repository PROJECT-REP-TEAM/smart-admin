package net.lab1024.smartadmin.service.config;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.service.module.support.redismq.RedisMqTopicEnum;
import net.lab1024.smartadmin.service.module.support.redismq.RedisMsgHandler;
import net.lab1024.smartadmin.service.util.SmartBaseEnumUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * [  ]
 *
 * @author 罗伊
 * @date
 */
@Slf4j
@Configuration
public class SmartRedisMqConfig {

    @Value("${redis.mq.topic:SmartAdmin}")
    private String topic;
    @Value("${redis.mq.scanPath:net.lab1024.smartadmin.service}")
    private String scanPath;

    /**
     * redis topic
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        boolean checkEnum = SmartBaseEnumUtil.checkEnum(topic, RedisMqTopicEnum.class);
        if (!checkEnum) {
            log.error("topic:{},系统暂未定义", topic);
            throw new RuntimeException("无效的redis topic");
        }
        container.addMessageListener(listenerAdapter, new PatternTopic(topic));
        return container;
    }

    /**
     * redis消息处理类
     * @return
     */
    @Bean
    public RedisMsgHandler redisMsgHandler() {
        return new RedisMsgHandler(scanPath);
    }

    /**
     * redis消息处理方法
     * @param receiver
     * @return
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(RedisMsgHandler receiver) {
        return new MessageListenerAdapter(receiver, RedisMsgHandler.METHOD_NAME);
    }
}
