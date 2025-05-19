package org.project.springbootstarter.service;

import org.project.springbootstarter.model.Question;
import org.project.springbootstarter.model.Quiz;
import org.project.springbootstarter.pojo.QuestionDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    ResponseEntity<String> addQuestionToQuiz(List<QuestionDto> questions, Long quizId);
    Optional<Question> getQuestionById(Long questionId);
    List<Question> findByQuiz(Quiz quiz);
}
