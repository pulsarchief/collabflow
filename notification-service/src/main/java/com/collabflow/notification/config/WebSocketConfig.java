// notification-service/src/main/java/com/collabflow/notification/config/WebSocketConfig.java
package com.collabflow.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket endpoint: ws://localhost:8083/ws
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*"); 
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Clients subscribe to /topic/project.{projectId}
        registry.enableSimpleBroker("/topic");
        // Any messages sent from clients to /app/... (if needed)
        registry.setApplicationDestinationPrefixes("/app");
    }
}
