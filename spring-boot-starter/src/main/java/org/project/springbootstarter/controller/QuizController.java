package org.project.springbootstarter.controller;
import org.project.springbootstarter.exception.Error;
import org.project.springbootstarter.exception.ServiceException;
import org.project.springbootstarter.model.Quiz;
import org.project.springbootstarter.pojo.AddQuestionsDto;
import org.project.springbootstarter.pojo.QuizDto;
import org.project.springbootstarter.service.QuestionServiceImpl;
import org.project.springbootstarter.service.QuizServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api-server/v1")
public class QuizController {
    @Autowired
    private QuizServiceImpl quizService;
    @Autowired
    private QuestionServiceImpl questionService;

    @PostMapping("/quizzes")
    public ResponseEntity<QuizDto> saveQuiz(@RequestBody Quiz quiz) {
        return quizService.save(quiz);
    }

    @GetMapping("/quizzes/{id}")
    public ResponseEntity<QuizDto> getQuiz(@PathVariable Long id) {
        Optional<Quiz> quizOpt= quizService.findById(id);
        if (quizOpt.isEmpty()) throw new ServiceException(Error.QUIZ_NOT_FOUND);
        Quiz quiz = quizOpt.get();
        QuizDto quizDto = QuizDto.builder().id(quiz.getId()).createdAt(quiz.getCreatedAt())
                .metadata(quiz.getMetadata()).title(quiz.getTitle()).build();
        return ResponseEntity.ok(quizDto);
    }

    @PostMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<String> addQuestionsToQuiz(@PathVariable Long quizId, @RequestBody AddQuestionsDto questions) {
        return questionService.addQuestionToQuiz(questions.getQuestions(), quizId);
    }
}
