// task-service/src/main/java/com/collabflow/task/model/Task.java
package com.collabflow.task.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Task implements Serializable {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    // assigned user id from user-service (optional)
    private UUID assignedTo;

    @Column(nullable = false)
    private String title;

    private String description;

    // e.g. BACKLOG, IN_PROGRESS, DONE
    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Instant updatedAt;
}
