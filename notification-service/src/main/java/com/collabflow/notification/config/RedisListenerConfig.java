// notification-service/src/main/java/com/collabflow/notification/config/RedisListenerConfig.java
package com.collabflow.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import com.collabflow.notification.service.TaskUpdateListener;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RedisListenerConfig {

    private final RedisConnectionFactory connectionFactory;
    private final TaskUpdateListener taskUpdateListener;

    @Bean
    public ChannelTopic taskUpdatesTopic() {
        // Must match the channel name used in task-service
        // (convertAndSend("task-updates", ...))
        return new ChannelTopic("task-updates");
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(ChannelTopic taskUpdatesTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(taskUpdateListener, taskUpdatesTopic);
        return container;
    }
}
