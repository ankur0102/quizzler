package org.project.springbootstarter.service;

import org.project.springbootstarter.model.Answer;
import org.project.springbootstarter.model.Player;
import org.project.springbootstarter.pojo.AnswerRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AnswerService {
    ResponseEntity<String> processAnswer(AnswerRequestDto answerRequestDto);
    List<Answer> findByPlayerIn(List<Player> players);
}
