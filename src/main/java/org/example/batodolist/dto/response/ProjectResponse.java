package org.example.batodolist.dto.response;

import org.example.batodolist.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private User manager;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
