package org.example.batodolist.dto.response;

import lombok.Data;
import org.example.batodolist.model.Task;

import java.time.LocalDateTime;

@Data
public class ReminderResponse {
    private Long id;
    private Task task;
    private LocalDateTime reminderTime;
    private LocalDateTime createdAt;
}
