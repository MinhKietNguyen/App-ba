package org.example.batodolist.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskCheckListRequest {
    private String title;

    private String description;

    private LocalDateTime completedAt;

    private Integer position;
}
