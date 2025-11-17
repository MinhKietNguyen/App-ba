package org.example.batodolist.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectUpdateRequest {
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
