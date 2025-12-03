package org.example.batodolist.dto.request;

import lombok.Data;
import org.example.batodolist.dto.TaskDTO;

import java.time.LocalDateTime;

@Data
public class TaskCheckListUpdateRequest {
    private String taskName;

    private String title;

    private String description;

    private Boolean isCompleted;

    private LocalDateTime completedAt;

    private Integer position;
}
