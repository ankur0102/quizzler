package org.project.springbootstarter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.springbootstarter.exception.ServiceException;
import org.project.springbootstarter.model.*;
import org.project.springbootstarter.pojo.*;
import org.project.springbootstarter.repository.GameSessionRepository;
import org.project.springbootstarter.utils.Constants;
import org.project.springbootstarter.websocket.WebSocketHandler;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class GameSessionServiceImplTest {

    @InjectMocks
    private GameSessionServiceImpl gameSessionService;

    @Mock
    private GameSessionRepository gameSessionRepository;

    @Mock
    private PlayerServiceImpl playerService;

    @Mock
    private QuizServiceImpl quizService;

    @Mock
    private WebSocketHandler webSocketHandler;

    @Mock
    private AnswerServiceImpl answerService;

    @Test
    void testCreateNewSession() {
        SessionRequest sessionRequest = SessionRequest.builder()
                .nickname("Player1")
                .build();
        GameSession gameSession = GameSession.builder()
                .id("abc")
                .status(Constants.NEW)
                .build();
        Mockito.when(gameSessionRepository.save(any(GameSession.class))).thenReturn(gameSession);
        Player player = Player.builder()
                .id(1L)
                .nickname("Player1")
                .build();
        Mockito.when(playerService.createNewPlayer("Player1", gameSession)).thenReturn(player);
        List<Quiz> quizzes = List.of(
                Quiz.builder().id(1L).title("Quiz 1").build(),
                Quiz.builder().id(2L).title("Quiz 2").build()
        );
        Mockito.when(quizService.getAllQuizzes()).thenReturn(quizzes);
        SessionResponseDto response = gameSessionService.createNewSession(sessionRequest);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("abc", response.getSessionId());
        Assertions.assertEquals("Player1", response.getNickname());
        Assertions.assertEquals(2, response.getQuizzes().size());
    }

    @Test
    void testStartGame() {
        String sessionId = "abc";
        GameSession gameSession = GameSession.builder()
                .id(sessionId)
                .status(Constants.NEW)
                .build();
        Mockito.when(gameSessionRepository.findById(sessionId)).thenReturn(Optional.of(gameSession));
        Quiz quiz = Quiz.builder()
                .id(1L)
                .title("Test Quiz")
                .build();
        Mockito.when(quizService.findById(1L)).thenReturn(Optional.of(quiz));
        StartRequestDto startRequestDto = new StartRequestDto();
        startRequestDto.setQuiz(QuizDto.builder().id(1L).build());
        ResponseEntity<String> response = gameSessionService.startGame(sessionId, startRequestDto);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("SUCCESS", response.getBody());
    }

    @Test
    void testStartGameWithException() {
        String sessionId = "abc";
        Mockito.when(gameSessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        StartRequestDto startRequestDto = new StartRequestDto();
        startRequestDto.setQuiz(QuizDto.builder().id(1L).build());
        Assertions.assertThrows(ServiceException.class, () -> gameSessionService.startGame(sessionId, startRequestDto));
    }

    @Test
    void testIncrementQuestion() {
        String sessionId = "abc";
        List<Question> questions = List.of(Question.builder().id(1L).build(), Question.builder().id(2L).build());
        Quiz quiz = Quiz.builder().questions(questions).id(1L).build();
        GameSession gameSession = GameSession.builder()
                .id(sessionId)
                .currentQuestionIndex(0)
                .quiz(quiz)
                .build();
        Mockito.when(gameSessionRepository.findById(sessionId)).thenReturn(Optional.of(gameSession));
        Mockito.when(gameSessionRepository.save(any(GameSession.class))).thenReturn(gameSession);
        NextQuestionRequestDto nextQuestionRequestDto = new NextQuestionRequestDto();
        ResponseEntity<String> response = gameSessionService.incrementQuestion(sessionId, nextQuestionRequestDto);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("SUCCESS", response.getBody());
        Mockito.verify(webSocketHandler, Mockito.times(1)).sendNextQuestion(any(GameSession.class));
    }

    @Test
    void testAddPlayer() {
        String sessionId = "abc";
        GameSession gameSession = GameSession.builder()
                .id(sessionId)
                .status(Constants.NEW)
                .build();
        Mockito.when(gameSessionRepository.findById(sessionId)).thenReturn(Optional.of(gameSession));
        Player player = Player.builder()
                .id(1L)
                .nickname("Player2")
                .build();
        Mockito.when(playerService.createNewPlayer("Player2", gameSession)).thenReturn(player);
        JoinRequestDto joinRequestDto = JoinRequestDto.builder()
                .nickname("Player2")
                .build();
        ResponseEntity<JoinResponseDto> response = gameSessionService.addPlayer(sessionId, joinRequestDto);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.getBody().getPlayerId());
        Assertions.assertEquals("Player2", response.getBody().getNickname());
    }

    @Test
    void testGetLeaderBoard() {
        String sessionId = "abc";
        GameSession gameSession = GameSession.builder()
                .id(sessionId)
                .build();

        Mockito.when(gameSessionRepository.findById(sessionId)).thenReturn(Optional.of(gameSession));

        List<Player> players = List.of(
                Player.builder().id(1L).nickname("Player1").build(),
                Player.builder().id(2L).nickname("Player2").build()
        );
        Mockito.when(playerService.getPlayersByGameSession(gameSession)).thenReturn(players);

        List<Answer> answers = List.of(
                Answer.builder().player(players.get(0)).correct(true).build(),
                Answer.builder().player(players.get(1)).correct(false).build()
        );
        Mockito.when(answerService.findByPlayerIn(players)).thenReturn(answers);

        ResponseEntity<LeaderBoardDto> response = gameSessionService.getLeaderBoard(sessionId);

        Assertions.assertNotNull(response);
    }
}