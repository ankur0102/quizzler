package org.project.springbootstarter.service;

import org.project.springbootstarter.model.Quiz;
import org.project.springbootstarter.pojo.QuizDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface QuizService {
    Optional<Quiz> findById(Long id);

    ResponseEntity<QuizDto> save(Quiz quiz);

    List<Quiz> getAllQuizzes();
}
