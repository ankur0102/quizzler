package org.project.springbootstarter.repository;

import org.project.springbootstarter.model.Answer;
import org.project.springbootstarter.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByPlayerIn(List<Player> players);
    boolean existsByPlayerIdAndQuestionId(Long playerId, Long questionId);
}
