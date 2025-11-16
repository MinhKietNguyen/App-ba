package org.example.batodolist.dto.request;

import lombok.Data;
import org.example.batodolist.model.Task;

import java.time.LocalDateTime;

@Data
public class ReminderRequest {
    private String taskName;
    private LocalDateTime reminderTime;
}
