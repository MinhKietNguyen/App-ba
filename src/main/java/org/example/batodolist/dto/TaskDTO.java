package org.example.batodolist.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.example.batodolist.common.TaskPriority;
import org.example.batodolist.common.TaskStatus;

import java.time.LocalDate;

@Data
public class TaskDTO {
    private Long id;
    private ProjectDTO project;
    private String name;
    private String description;
    private LocalDate dueDate;
    private TaskStatus status;
    private TaskPriority priority;

    @JsonIgnoreProperties({"project"})
    private ProjectMemberDTO assignedTo;

    private UserDTO creator;
}
