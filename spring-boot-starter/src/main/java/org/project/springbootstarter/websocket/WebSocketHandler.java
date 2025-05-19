package org.project.springbootstarter.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.project.springbootstarter.model.GameSession;
import org.project.springbootstarter.model.Question;
import org.project.springbootstarter.pojo.QuestionWsRequest;
import org.project.springbootstarter.service.QuestionServiceImpl;
import org.project.springbootstarter.service.RedisServiceImpl;
import org.project.springbootstarter.utils.Constants;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final QuestionServiceImpl questionService;
    private final RedisServiceImpl redisService;
    private static final Logger logger = LogManager.getLogger(WebSocketHandler.class);

    private Map<String, Set<WebSocketSession>> sessions = new HashMap<String, Set<WebSocketSession>>();

    public WebSocketHandler(QuestionServiceImpl questionService, RedisServiceImpl redisService) {
        this.questionService = questionService;
        this.redisService = redisService;
    }

    public void startGameSession(GameSession gameSession) {
        logger.info("WebSocketHandler: Starting game session {}", gameSession.getId());
        sendNextQuestion(gameSession);
    }

    public void sendEndMessage(GameSession gameSession) {
        QuestionWsRequest questionRequest = QuestionWsRequest
                .builder()
                .redirectToLeaderBoard(true)
                .build();

        final String jsonResponse[] = {"SYSTEM_FAILURE"};

        try {
            jsonResponse[0] = objectMapper.writeValueAsString(questionRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Set<WebSocketSession> sessionList = sessions.get(gameSession.getId());
        sessionList.parallelStream().forEach(ws -> {
            try {
                sendMessageWithRetry(ws, jsonResponse[0], Constants.WEBSOCKET_RETRY_ATTEMPTS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    public void sendNextQuestion(GameSession gameSession) {
        logger.info("WebSocketHandler: Sending next question, sessionId: {}, index: {}",
                gameSession.getId(), gameSession.getCurrentQuestionIndex());

        if (Constants.COMPLETED.equals(gameSession.getStatus())){
            sendEndMessage(gameSession);
            return;
        }

        List<Question> questions = questionService.findByQuiz(gameSession.getQuiz());

        if (questions.size() == gameSession.getCurrentQuestionIndex()) return;

        Question question = questions.get(gameSession.getCurrentQuestionIndex());
        Set<WebSocketSession> sessionList = sessions.get(gameSession.getId());
        logger.info("WebSocketHandler: number of sessions {}", sessionList);
        Long currentTime = System.currentTimeMillis();
        Long endTime = currentTime + Constants.TEN_SECONDS_IN_MILLIS;

        QuestionWsRequest questionRequest = QuestionWsRequest
                .builder()
                .question(question)
                .currentIndex(gameSession.getCurrentQuestionIndex())
                .totalQuestions(questions.size())
                .endTime(endTime)
                .redirectToLeaderBoard(false)
                .build();

        String redisKey = getRedisKey(gameSession.getId(), question.getId());
        logger.info("WebSocketHandler: Saving redis key: {}, startTime: {}, endTime: {}", redisKey, currentTime, questionRequest.getEndTime());
        redisService.save(redisKey, questionRequest.getEndTime());

        final String jsonResponse[] = {"SYSTEM_FAILURE"};

        try {
            jsonResponse[0] = objectMapper.writeValueAsString(questionRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        sessionList.parallelStream().forEach(ws -> {
            try {
                sendMessageWithRetry(ws, jsonResponse[0], Constants.WEBSOCKET_RETRY_ATTEMPTS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    public void sendMessageWithRetry(WebSocketSession ws, String message, Long retryAttempts) throws InterruptedException {
        boolean retry = retryAttempts != 0;
        try {
            ws.sendMessage(new TextMessage(message));
        } catch (IllegalStateException | IOException e) {
            logger.warn("Websocket exception failure for connectionId {}", ws.getId());
            if (retry) {
                Thread.sleep(Constants.WEBSOCKET_RETRY_DELAY_MILLIS);
                sendMessageWithRetry(ws, message, retryAttempts - 1);
            }
            else {
                logger.error("Websocket exception failure for connectionId {}", ws.getId());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = (String) session.getAttributes().get("sessionId");
        logger.info("New connection established: {}, sessionId: {}", session.getId(), sessionId);

        Set<WebSocketSession> sessionList = sessions.getOrDefault(sessionId, new HashSet<>());
        sessionList.add(session);
        sessions.put(sessionId, sessionList);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String clientMessage = message.getPayload(); // Message received from the client
        System.out.println("Received from client: " + clientMessage);

        Question question = questionService.getQuestionById(1L).get();
        String jsonResponse = objectMapper.writeValueAsString(QuestionWsRequest
                .builder()
                .question(question)
                .endTime(System.currentTimeMillis())
                .build()
        );
        session.sendMessage(new TextMessage(jsonResponse));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Connection closed: " + session.getId());
    }

    public String getRedisKey(String sessionId, Long questionId) {
        return String.format("%s#%d", sessionId, questionId);
    }
}