package org.example.batodolist.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tasklabels")
@Data
public class TaskLabel {
    @EmbeddedId
    private TaskLabelKey id;

    @ManyToOne
    @MapsId("taskId")
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @MapsId("labelId")
    @JoinColumn(name = "label_id")
    private Label label;
}
