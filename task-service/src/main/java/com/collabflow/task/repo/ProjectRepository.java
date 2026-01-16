// task-service/src/main/java/com/collabflow/task/repo/ProjectRepository.java
package com.collabflow.task.repo;

import com.collabflow.task.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
}
