package org.example.batodolist.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.batodolist.common.TaskPriority;
import org.example.batodolist.common.TaskStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.todo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority = TaskPriority.medium;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private ProjectMember assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(nullable = false)
    private Integer progress;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskCheckList> checklists;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reminder> reminders;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private List<TaskLabel> taskLabels = new ArrayList<>();

    // Check constraint validation
    @PrePersist
    @PreUpdate
    private void validateTask() {
        if (project != null && assignedTo == null) {
            throw new IllegalStateException("Project tasks must have assignedTo");
        }
        if (project != null && createdBy != null) {
            throw new IllegalStateException("Project tasks cannot have createdBy");
        }
        if (project == null && assignedTo != null) {
            throw new IllegalStateException("Personal tasks cannot have assignedTo");
        }
        if (project == null && createdBy == null) {
            throw new IllegalStateException("Personal tasks must have createdBy");
        }
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }
    }

    public boolean isPersonalTask() {
        return project == null && createdBy != null;
    }

    public boolean isProjectTask() {
        return project != null && assignedTo != null;
    }
}
