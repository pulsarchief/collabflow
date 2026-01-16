package com.collabflow.task.controller;

import com.collabflow.common.dto.ProjectCreateRequest;
import com.collabflow.task.model.Project;
import com.collabflow.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<?> createProject(
            @RequestBody ProjectCreateRequest req,
            @RequestHeader("X-User-Id") String userIdHeader) {

        UUID ownerId = UUID.fromString(userIdHeader);
        Project p = taskService.createProject(req, ownerId);
        return ResponseEntity.ok(p);
    }

    @GetMapping("/{id}")                                          
    public ResponseEntity<?> getProject(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(taskService.getProject(id));
    }
}
                    