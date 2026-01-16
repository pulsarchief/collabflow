// task-service/src/main/java/com/collabflow/task/service/TaskService.java
package com.collabflow.task.service;

import com.collabflow.common.dto.ProjectCreateRequest;
import com.collabflow.common.dto.TaskCreateRequest;
import com.collabflow.common.dto.TaskEvent;
import com.collabflow.common.dto.TaskUpdateRequest;
import com.collabflow.task.model.Project;
import com.collabflow.task.model.Task;
import com.collabflow.task.repo.ProjectRepository;
import com.collabflow.task.repo.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ---------- PROJECTS ----------


    public Project createProject(ProjectCreateRequest req, UUID ownerId) {
        Project p = new Project();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setOwnerId(ownerId);
        return projectRepository.save(p);
    }

    public Project getProject(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }

    // ---------- TASKS + CACHE ----------


    @Cacheable(cacheNames = "projectTasks", key = "#projectId")
    public List<Task> getTasksForProject(UUID projectId) {
        Project project = getProject(projectId);
        return taskRepository.findByProject(project);
    }

    @CacheEvict(cacheNames = "projectTasks", key = "#root.args[0].projectId")
    public Task createTask(TaskCreateRequest req, String updatedByUserId) {
        Project project = getProject(UUID.fromString(req.getProjectId()));

        Task t = new Task();
        t.setProject(project);
        t.setTitle(req.getTitle());
        t.setDescription(req.getDescription());
        t.setStatus("BACKLOG");
        t.setUpdatedAt(Instant.now());

        if (req.getAssignedTo() != null && !req.getAssignedTo().isBlank()) {
            t.setAssignedTo(UUID.fromString(req.getAssignedTo()));
        }

        Task saved = taskRepository.save(t);

        publishEvent(TaskEvent.builder()
                .type("CREATED")
                .taskId(saved.getId().toString())
                .projectId(project.getId().toString())
                .status(saved.getStatus())
                .updatedBy(updatedByUserId)
                .build());

        return saved;
    }

    @CacheEvict(cacheNames = "projectTasks", key = "#projectId")
    public Task updateTask(UUID taskId, UUID projectId, TaskUpdateRequest req, String updatedByUserId) {
        Task t = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (req.getTitle() != null) {
            t.setTitle(req.getTitle());
        }
        if (req.getDescription() != null) {
            t.setDescription(req.getDescription());
        }
        if (req.getStatus() != null) {
            t.setStatus(req.getStatus());
        }

        t.setUpdatedAt(Instant.now());

        Task saved = taskRepository.save(t);

        publishEvent(TaskEvent.builder()
                .type("UPDATED")
                .taskId(saved.getId().toString())
                .projectId(saved.getProject().getId().toString())
                .status(saved.getStatus())
                .updatedBy(updatedByUserId)
                .build());

        return saved;
    }

    // ---------- REDIS PUB/SUB ----------


    private void publishEvent(TaskEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend("task-updates", payload);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to publish task event to Redis", e);
        }
    }
}
