package org.example.batodolist.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "messages")
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ProjectMember user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Message parent;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Relationships
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Message> replies;

    // Helper methods
    public boolean isReply() {
        return parent != null;
    }

    public boolean hasReplies() {
        return replies != null && !replies.isEmpty();
    }
}
