package org.project.springbootstarter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@Entity
@Table(name = "players")
@AllArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private GameSession gameSession;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "joined_at", nullable = false)
    private Long joinedAt;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;

    public Player() {}

    @PrePersist
    public void prePersist() {
        this.joinedAt = System.currentTimeMillis();
    }
}