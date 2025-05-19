package org.project.springbootstarter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.springbootstarter.exception.ServiceException;
import org.project.springbootstarter.model.Question;
import org.project.springbootstarter.model.Quiz;
import org.project.springbootstarter.pojo.QuestionDto;
import org.project.springbootstarter.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceImplTest {

    @InjectMocks
    private QuestionServiceImpl questionService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuizServiceImpl quizService;

    @Test
    void testAddQuestionToQuizThrowsException() {
        Long quizId = 1L;
        List<QuestionDto> questions = List.of(
                QuestionDto.builder().text("Question 1").build(),
                QuestionDto.builder().text("Question 2").build()
        );
        Mockito.when(quizService.findById(quizId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceException.class, () -> questionService.addQuestionToQuiz(questions, quizId));
        Mockito.verify(questionRepository, Mockito.never()).saveAll(any(List.class));
    }

    @Test
    void testGetQuestionById() {
        Long questionId = 1L;
        Question question = Question.builder().id(questionId).text("Sample Question").build();
        Mockito.when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        Optional<Question> result = questionService.getQuestionById(questionId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Sample Question", result.get().getText());
        Assertions.assertEquals(questionId, result.get().getId());
    }

    @Test
    void testGetQuestionByIdNotFound() {
        Long questionId = 1L;
        Mockito.when(questionRepository.findById(questionId)).thenReturn(Optional.empty());
        Optional<Question> result = questionService.getQuestionById(questionId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testFindByQuiz() {
        Quiz quiz = Quiz.builder().id(1L).title("Sample Quiz").build();
        List<Question> questions = new ArrayList<>();
        questions.add(Question.builder().id(1L).text("Question 1").build());
        questions.add(Question.builder().id(2L).text("Question 2").build());
        Mockito.when(questionRepository.findByQuiz(quiz)).thenReturn(questions);
        List<Question> result = questionService.findByQuiz(quiz);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Question 1", result.get(0).getText());
        Assertions.assertEquals("Question 2", result.get(1).getText());
    }

    @Test
    void testAddQuestionToQuiz() {
        Long quizId = 1L;
        Quiz quiz = Quiz.builder().id(quizId).title("Sample Quiz").build();

        List<QuestionDto> questionsDto = List.of(
                QuestionDto.builder().text("Question 1").build(),
                QuestionDto.builder().text("Question 2").build()
        );

        List<Question> questions = List.of(
                Question.builder().id(1L).text("Question 1").quiz(quiz).build(),
                Question.builder().id(2L).text("Question 2").quiz(quiz).build()
        );
        Mockito.when(quizService.findById(quizId)).thenReturn(Optional.of(quiz));
        Mockito.when(questionRepository.saveAll(Mockito.anyList())).thenReturn(questions);
        questionService.addQuestionToQuiz(questionsDto, quizId);
        questions.forEach(question -> Assertions.assertEquals(quiz, question.getQuiz()));
    }
}