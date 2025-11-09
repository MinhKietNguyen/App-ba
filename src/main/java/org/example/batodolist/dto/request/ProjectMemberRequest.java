package org.example.batodolist.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectMemberRequest {
    private String projectName;
    private String memberName;
}
