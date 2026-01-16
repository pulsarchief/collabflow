package com.collabflow.common.dto;

import lombok.Data;

@Data
public class TaskCreateRequest {
    private String projectId;
    private String title;
    private String description;
    private String assignedTo;
}
