package org.example.batodolist.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.example.batodolist.model.ProjectMember;

@Getter
@Setter
public class TaskRequest {
    private String projectName;
    private String name;
    private String description;
    private String assignedTo;
}
