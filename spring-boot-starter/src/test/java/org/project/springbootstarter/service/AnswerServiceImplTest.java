package org.project.springbootstarter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.springbootstarter.exception.ServiceException;
import org.project.springbootstarter.model.Answer;
import org.project.springbootstarter.model.Player;
import org.project.springbootstarter.model.Question;
import org.project.springbootstarter.pojo.AnswerRequestDto;
import org.project.springbootstarter.repository.AnswerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AnswerServiceImplTest {
    @InjectMocks
    private AnswerServiceImpl answerService;
    @Mock
    private AnswerRepository answerRepository;
    @Mock
    private RedisServiceImpl redisService;
    @Mock
    PlayerServiceImpl playerService;
    @Mock
    QuestionServiceImpl questionService;

    @Test
    void testProcessAnswer() {
        Long currentTime = System.currentTimeMillis();
        AnswerRequestDto answerRequestDto = AnswerRequestDto.builder()
                .playerId(1L)
                .questionId(1L)
                .answer("5")
                .submittedAt(currentTime - 5000)
                .sessionId("abc")
                .build();
        Mockito.when(redisService.get(Mockito.anyString())).thenReturn(currentTime);
        Mockito.when(answerRepository.existsByPlayerIdAndQuestionId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false);
        Mockito.when(playerService.getPlayerById(Mockito.anyLong())).thenReturn(Optional.of(new Player()));
        Mockito.when(questionService.getQuestionById(Mockito.anyLong())).thenReturn(Optional.of(new Question()));
        Mockito.when(answerRepository.save(Mockito.any())).thenReturn(new Answer());
        ResponseEntity<String> responseEntity =  answerService.processAnswer(answerRequestDto);
        Assertions.assertEquals("success", responseEntity.getBody());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testProcessAnswerWithException() {
        Long currentTime = System.currentTimeMillis();
        AnswerRequestDto answerRequestDto = AnswerRequestDto.builder()
                .playerId(1L)
                .questionId(1L)
                .answer("5")
                .submittedAt(currentTime + 5000)
                .sessionId("abc")
                .build();
        Mockito.when(redisService.get(Mockito.anyString())).thenReturn(currentTime);
        Assertions.assertThrows(ServiceException.class, () -> answerService.processAnswer(answerRequestDto));
    }

    @Test
    void testFindByPlayerIn() {
        List<Player> player = new ArrayList<>();
        player.add(Player.builder().id(1L).build());
        player.add(Player.builder().id(2L).build());
        List<Answer> answers = new ArrayList<>();
        answers.add(Answer.builder().id(1L).build());
        answers.add(Answer.builder().id(2L).build());
        Mockito.when(answerRepository.findByPlayerIn(player)).thenReturn(answers);
        List<Answer> playerAnswers = answerService.findByPlayerIn(player);
        Assertions.assertEquals(2, playerAnswers.size());
    }

    @Test
    void TestGetRedisKey() {
        String key = answerService.getRedisKey("abc", 5L);
        Assertions.assertEquals("abc#5", key);
    }
}
