package org.example.batodolist.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.example.batodolist.common.ProjectMemberRole;

@Getter
@Setter
public class ProjectMemberUpdateRequest {
    private String projectName;
    private String memberName;
    private ProjectMemberRole roleInProject;
}
