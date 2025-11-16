package org.example.batodolist.dto.request;

import lombok.Data;
import org.example.batodolist.common.UserRole;

@Data
public class UserUpdateRequest {
    private String username;
    private String email;
    private String password;
    private String fullName;
    private UserRole role;
}
