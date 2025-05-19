package org.project.springbootstarter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "answers", indexes = {
        @Index(name = "idx_player_question", columnList = "player_id, question_id"),
        @Index(name = "idx_submitted_at", columnList = "submitted_at")
})
@AllArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "selected_option", nullable = false)
    private String selectedOption;

    @Column(name = "submitted_at", nullable = false)
    private Long submittedAt;

    @Column(name = "correct", nullable = false)
    private Boolean correct;

    public Answer() {}
}