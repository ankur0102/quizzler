package org.project.springbootstarter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.springbootstarter.model.Quiz;
import org.project.springbootstarter.pojo.QuizDto;
import org.project.springbootstarter.repository.QuizRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class QuizServiceImplTest {

    @InjectMocks
    private QuizServiceImpl quizService;

    @Mock
    private QuizRepository quizRepository;

    @Test
    void testFindById() {
        Long quizId = 1L;
        Quiz quiz = Quiz.builder().id(quizId).title("Sample Quiz").build();
        Mockito.when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
        Optional<Quiz> result = quizService.findById(quizId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Sample Quiz", result.get().getTitle());
        Assertions.assertEquals(quizId, result.get().getId());
    }

    @Test
    void testFindByIdNotFound() {
        Long quizId = 1L;
        Mockito.when(quizRepository.findById(quizId)).thenReturn(Optional.empty());
        Optional<Quiz> result = quizService.findById(quizId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testSave() {
        Quiz quiz = Quiz.builder().title("New Quiz").build();
        Quiz savedQuiz = Quiz.builder().id(1L).title("New Quiz").build();
        Mockito.when(quizRepository.save(quiz)).thenReturn(savedQuiz);
        ResponseEntity<QuizDto> result = quizService.save(quiz);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(1L, Objects.requireNonNull(result.getBody()).getId());
        Assertions.assertEquals("New Quiz", result.getBody().getTitle());
    }

    @Test
    void testGetAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        quizzes.add(Quiz.builder().id(1L).title("Quiz 1").build());
        quizzes.add(Quiz.builder().id(2L).title("Quiz 2").build());
        Mockito.when(quizRepository.findAll()).thenReturn(quizzes);
        List<Quiz> result = quizService.getAllQuizzes();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Quiz 1", result.get(0).getTitle());
        Assertions.assertEquals("Quiz 2", result.get(1).getTitle());
    }
}