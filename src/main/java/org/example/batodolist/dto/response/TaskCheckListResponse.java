package org.example.batodolist.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.example.batodolist.dto.TaskDTO;
import org.example.batodolist.model.Task;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class TaskCheckListResponse {
    private Long id;

    private TaskDTO taskDTO;

    private String title;

    private String description;

    private Boolean isCompleted = false;

    private LocalDateTime completedAt;

    private Integer position;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
