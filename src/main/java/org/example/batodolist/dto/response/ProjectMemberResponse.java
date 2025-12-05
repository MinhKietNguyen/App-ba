package org.example.batodolist.dto.response;

import lombok.Data;
import org.example.batodolist.common.ProjectMemberRole;
import org.example.batodolist.dto.ProjectDTO;
import org.example.batodolist.dto.UserDTO;

import java.time.LocalDateTime;

@Data
public class ProjectMemberResponse {
    private Long id;
    private ProjectDTO project;
    private UserDTO user;
    private ProjectMemberRole roleInProject;
    private LocalDateTime joinedAt;
}
