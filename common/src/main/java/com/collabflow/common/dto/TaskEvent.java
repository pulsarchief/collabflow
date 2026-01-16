package com.collabflow.common.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class TaskEvent {
    private String type;
    private String taskId;
    private String projectId;
    private String status;
    private String updatedBy;
}
