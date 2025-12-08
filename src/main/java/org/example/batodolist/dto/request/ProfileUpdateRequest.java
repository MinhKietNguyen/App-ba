package org.example.batodolist.dto.request;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String username;
    private String email;
    private String password;  // New password (optional)
    private String fullName;
    // Note: role is not included - users cannot change their own role
}

