package org.example.batodolist.dto.response;

import org.example.batodolist.common.ProjectMemberRole;
import org.example.batodolist.model.Project;
import org.example.batodolist.model.User;

import java.time.LocalDateTime;

public class ProjectMemberResponse {
    private Long id;
    private Project project;
    private User user;
    private ProjectMemberRole roleInProject;
    private LocalDateTime joinedAt;
}
