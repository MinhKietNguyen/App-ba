package org.example.batodolist.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskUpdateRequest {
    private String projectName;
    private String name;
    private String description;
    private String assignedTo;
    private Integer progress;
}
