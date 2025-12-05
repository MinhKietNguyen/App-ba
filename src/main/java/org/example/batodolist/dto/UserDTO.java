package org.example.batodolist.dto;

import lombok.Data;
import org.example.batodolist.common.ProjectMemberRole;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
}
