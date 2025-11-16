package org.example.batodolist.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.example.batodolist.model.Project;

@Getter
@Setter
public class TaskResponse {
    private Long id;
    private String projectName;
    private String name;
    private String description;
    private String assignedTo;
    private Integer progress;
}
