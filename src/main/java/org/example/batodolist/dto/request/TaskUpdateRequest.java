package org.example.batodolist.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.example.batodolist.common.TaskPriority;
import org.example.batodolist.common.TaskStatus;

import java.time.LocalDate;

@Setter
@Getter
public class TaskUpdateRequest {
    private String name;
    private String description;
    private String assignedTo;
    private LocalDate dueDate;
    private TaskStatus status;
    private TaskPriority priority;
    private Integer progress;
}
