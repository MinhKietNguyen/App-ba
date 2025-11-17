package org.example.batodolist.dto;

import lombok.Data;
import org.example.batodolist.common.ProjectMemberRole;

import java.time.LocalDateTime;

@Data
public class ProjectMemberDTO {
    private Long id;
    private ProjectDTO project;
    private UserDTO user;
    private ProjectMemberRole roleInProject;
    private LocalDateTime joinedAt;
}
