// task-service/src/main/java/com/collabflow/task/repo/TaskRepository.java
package com.collabflow.task.repo;

import com.collabflow.task.model.Project;
import com.collabflow.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByProject(Project project);
}
