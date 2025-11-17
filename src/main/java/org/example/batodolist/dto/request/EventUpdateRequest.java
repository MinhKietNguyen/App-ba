package org.example.batodolist.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.example.batodolist.common.EventRecurrence;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventUpdateRequest {
    private String projectName;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String integrationSource;
    private EventRecurrence recurrence;
}
