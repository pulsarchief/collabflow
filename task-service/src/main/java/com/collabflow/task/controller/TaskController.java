// task-service/src/main/java/com/collabflow/task/controller/TaskController.java
package com.collabflow.task.controller;

import com.collabflow.common.dto.TaskCreateRequest;
import com.collabflow.common.dto.TaskUpdateRequest;
import com.collabflow.task.model.Task;
import com.collabflow.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<Task>> getTasksForProject(@PathVariable("projectId") UUID projectId) {
        return ResponseEntity.ok(taskService.getTasksForProject(projectId));
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(
            @RequestBody TaskCreateRequest req,
            @RequestHeader("X-User-Id") String userId) {
        
        Task t = taskService.createTask(req, userId);
        return ResponseEntity.ok(t);
    }

    @PatchMapping("/tasks/{taskId}")
    public ResponseEntity<Task> updateTask(
            @PathVariable("taskId") UUID taskId,
            @RequestParam("projectId") UUID projectId,
            @RequestBody TaskUpdateRequest req,
            @RequestHeader("X-User-Id") String userId) {

        Task t = taskService.updateTask(taskId, projectId, req, userId);
        return ResponseEntity.ok(t);
    }
}
