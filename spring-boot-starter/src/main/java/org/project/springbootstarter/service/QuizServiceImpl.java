package org.project.springbootstarter.service;

import org.project.springbootstarter.model.Quiz;
import org.project.springbootstarter.pojo.QuizDto;
import org.project.springbootstarter.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public Optional<Quiz> findById(Long id) {
        return quizRepository.findById(id);
    }

    public ResponseEntity<QuizDto> save(Quiz quiz) {
        quiz = quizRepository.save(quiz);
        return ResponseEntity.ok(QuizDto.builder().id(quiz.getId()).title(quiz.getTitle()).metadata(quiz.getMetadata())
                .createdAt(quiz.getCreatedAt()).build());
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }
}
