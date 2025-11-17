package org.example.batodolist.dto.response;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LabelResponse {
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private  String name;
    private  String color;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
