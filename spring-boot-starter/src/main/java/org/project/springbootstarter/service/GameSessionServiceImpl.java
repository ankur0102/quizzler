package org.project.springbootstarter.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.project.springbootstarter.exception.Error;
import org.project.springbootstarter.exception.ServiceException;
import org.project.springbootstarter.model.Answer;
import org.project.springbootstarter.model.GameSession;
import org.project.springbootstarter.model.Player;
import org.project.springbootstarter.model.Quiz;
import org.project.springbootstarter.pojo.*;
import org.project.springbootstarter.repository.GameSessionRepository;
import org.project.springbootstarter.utils.Constants;
import org.project.springbootstarter.websocket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameSessionServiceImpl implements GameSessionService {
    @Autowired
    private GameSessionRepository gameSessionRepository;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private QuizService quizService;
    @Autowired
    private WebSocketHandler webSocketHandler;
    @Autowired
    private AnswerService answerService;

    private final Logger logger = LogManager.getLogger(GameSessionServiceImpl.class.getName());

    public SessionResponseDto createNewSession(SessionRequest sessionRequest) {
        GameSession gameSession = gameSessionRepository.save(GameSession.builder().status(Constants.NEW).build());

        logger.info("GameSessionService: {}", gameSession.getId());
        Player p = playerService.createNewPlayer(sessionRequest.getNickname(), gameSession);

        List<Quiz> quizzes = quizService.getAllQuizzes();

        return SessionResponseDto.builder()
                .playerId(p.getId()).nickname(p.getNickname()).quizzes(quizzes.stream()
                        .map(quiz -> QuizDto.builder().id(quiz.getId()).title(quiz.getTitle())
                                .metadata(quiz.getMetadata()).createdAt(quiz.getCreatedAt()).build())
                        .collect(Collectors.toList()))
                .sessionId(gameSession.getId()).build();
    }

    // Todo: CHECK THE BAD PATH ALSO LIKE IF THE GAME HOST != PLAYER
    public ResponseEntity<String> startGame(String sessionId, StartRequestDto startRequestDto) {
        Optional<GameSession> gameSessionOpt = gameSessionRepository.findById(sessionId);

        if (gameSessionOpt.isEmpty()) throw new ServiceException(Error.SESSION_NOT_FOUND);

        GameSession gameSession = gameSessionOpt.get();

        // set game session
        gameSession.setStatus(Constants.STARTED);
        gameSession.setStartedAt(System.currentTimeMillis());
        gameSession.setCurrentQuestionIndex(0);

        Optional<Quiz> quizOpt = quizService.findById(startRequestDto.getQuiz().getId());
        if (quizOpt.isEmpty()) throw new ServiceException(Error.QUIZ_NOT_FOUND);

        gameSession.setQuiz(quizOpt.get());
        gameSession = gameSessionRepository.save(gameSession);

        webSocketHandler.startGameSession(gameSession);

        return ResponseEntity.ok("SUCCESS");
    }

    public ResponseEntity<String> incrementQuestion(String sessionId, NextQuestionRequestDto nextQuestionRequestDto) {
        Optional<GameSession> gameSessionOpt = gameSessionRepository.findById(sessionId);
        if (gameSessionOpt.isEmpty()) throw new ServiceException(Error.SESSION_NOT_FOUND);
        GameSession gameSession = gameSessionOpt.get();
        int quizSize = gameSession.getQuiz().getQuestions().size();
        int index = gameSession.getCurrentQuestionIndex() + 1;
        gameSession.setCurrentQuestionIndex(index);

        if (index == quizSize) {
            logger.info("GameSessionService: session id {} with quiz id {} is completed", gameSession.getId(), gameSession.getQuiz().getId());
            gameSession.setStatus(Constants.COMPLETED);
        }

        gameSession = gameSessionRepository.save(gameSession);
        // call the streamer
        logger.info("GameSessionService: session is {}, quiz: {}", gameSession.getId(), gameSession.getQuiz());
        webSocketHandler.sendNextQuestion(gameSession);
        return ResponseEntity.ok("SUCCESS");
    }

    // Todo: fix this, throw error
    public ResponseEntity<JoinResponseDto> addPlayer(String sessionId, JoinRequestDto joinRequestDto) {
        GameSession gameSession = gameSessionRepository.findById(sessionId).get();
        if (Constants.NEW.equals(gameSession.getStatus())) {
            Player player = playerService.createNewPlayer(joinRequestDto.getNickname(), gameSession);
            // connect new player to websocket
            return ResponseEntity.ok(JoinResponseDto.builder().playerId(player.getId()).nickname(player.getNickname()).sessionId(gameSession.getId()).websocketEP("ws").build());
        } else return null;
    }

    public ResponseEntity<LeaderBoardDto> getLeaderBoard(String sessionId) {
        Optional<GameSession> gameSessionOpt = gameSessionRepository.findById(sessionId);
        if (gameSessionOpt.isEmpty()) throw new ServiceException(Error.SESSION_NOT_FOUND);

        List<Player> players = playerService.getPlayersByGameSession(gameSessionOpt.get());

        List<Answer> answers = answerService.findByPlayerIn(players);

        return ResponseEntity.ok(getLeaderBoardV2(answers, players));
    }

    public LeaderBoardDto getLeaderBoardV2(List<Answer> answers, List<Player> players) {

        if (answers.isEmpty()) {
            return new LeaderBoardDto(
                    players.stream().filter(player -> !"host".equals(player.getNickname())).map(player ->
                            PlayerScore.builder()
                                    .score(0L)
                                    .playerId(player.getId())
                                    .playerName(player.getNickname())
                                    .build()).collect(Collectors.toList()));
        }

        Map<Long, PlayerScore> playerScores = new HashMap<>();

        players.forEach(player -> {
           if (!"host".equals(player.getNickname()) && !playerScores.containsKey(player.getId())) {
               playerScores.put(player.getId(), PlayerScore.builder().playerId(player.getId()).playerName(player.getNickname()).score(0L).build());
           }
        });


        answers.stream().filter(Answer::getCorrect).forEach(ans -> {
            PlayerScore playerScore = playerScores.get(ans.getPlayer().getId());
            playerScore.setScore(playerScore.getScore() + 1);
            playerScores.put(ans.getPlayer().getId(), playerScore);
        });

        List<PlayerScore> result = new ArrayList<>();

        for (Map.Entry<Long, PlayerScore> entry : playerScores.entrySet()) {
            result.add(entry.getValue());
        }

        result.sort((p2, p1) -> p1.getScore().intValue() - p2.getScore().intValue());

        return new LeaderBoardDto(result);
    }
}
