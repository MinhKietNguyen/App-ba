package org.example.batodolist.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class TaskLabelKey {
    private Long taskId;
    private Long labelId;
}
