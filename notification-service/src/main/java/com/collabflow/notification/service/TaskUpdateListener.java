// notification-service/src/main/java/com/collabflow/notification/service/TaskUpdateListener.java
package com.collabflow.notification.service;

import com.collabflow.common.dto.TaskEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskUpdateListener implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody());
            TaskEvent event = objectMapper.readValue(json, TaskEvent.class);

            // Destination topic based on projectId
            String destination = "/topic/project." + event.getProjectId();

            // Broadcast to all subscribed WebSocket clients
            messagingTemplate.convertAndSend(destination, event);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error processing task update message from Redis", e);
        }
    }
}
