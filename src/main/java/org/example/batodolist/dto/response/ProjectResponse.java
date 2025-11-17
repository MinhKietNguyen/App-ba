package org.example.batodolist.dto.response;

import lombok.Data;
import org.example.batodolist.dto.UserDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private UserDTO manager;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
