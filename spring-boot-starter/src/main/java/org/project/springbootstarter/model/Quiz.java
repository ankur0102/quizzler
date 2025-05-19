package org.project.springbootstarter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Entity
@Table(name = "quizzes")
@AllArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob // Assuming metadata is a large JSON or text blob
    private String metadata;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @JsonIgnore
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameSession> gameSessions;

    @PrePersist
    public void prePersist() {
        this.createdAt = System.currentTimeMillis();
    }

    public Quiz() {}
}
