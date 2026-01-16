package com.collabflow.common.dto;

import lombok.Data;

@Data
public class TaskUpdateRequest {
    private String status;
    private String title;
    private String description;
}
