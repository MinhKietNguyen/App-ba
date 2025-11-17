package org.example.batodolist.dto.response;

import lombok.Data;
import org.example.batodolist.dto.TaskDTO;
import org.example.batodolist.model.Task;

import java.time.LocalDateTime;

@Data
public class ReminderResponse {
    private Long id;
    private TaskDTO task;
    private LocalDateTime reminderTime;
    private LocalDateTime createdAt;
}
