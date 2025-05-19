package org.project.springbootstarter.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.project.springbootstarter.pojo.*;
import org.project.springbootstarter.service.AnswerServiceImpl;
import org.project.springbootstarter.service.GameSessionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api-server/v1")
@RestController
public class SessionController {
    @Autowired
    GameSessionServiceImpl gameSessionService;
    @Autowired
    AnswerServiceImpl answerService;

    private static final Logger logger = LogManager.getLogger(SessionController.class);

    @PostMapping("/sessions")
    public ResponseEntity<SessionResponseDto> initSession(@RequestBody SessionRequest sessionRequest) {
        return ResponseEntity.ok(gameSessionService.createNewSession(sessionRequest));
    }

    @PostMapping("/sessions/{sessionId}/start")
    public ResponseEntity<String> startGame(@PathVariable String sessionId, @RequestBody StartRequestDto startRequestDto) {
        logger.info("SessionController: Starting game session {}", sessionId);
        return gameSessionService.startGame(sessionId, startRequestDto);
    }

    // Todo: Fix This
    @PostMapping("/sessions/{sessionId}/join")
    public ResponseEntity<JoinResponseDto> addPlayer(@PathVariable String sessionId, @RequestBody JoinRequestDto joinRequestDto) {
        return gameSessionService.addPlayer(sessionId, joinRequestDto);
    }

    @PostMapping("/sessions/{sessionId}/next")
    public ResponseEntity<String> nextQuestion(@PathVariable String sessionId, @RequestBody NextQuestionRequestDto nextQuestionRequestDto) {
        return gameSessionService.incrementQuestion(sessionId, nextQuestionRequestDto);
    }

    @PostMapping("/sessions/{sessionId}/answer")
    public ResponseEntity<String> addAnswer(@PathVariable String sessionId, @RequestBody AnswerRequestDto answerRequest) {
        return answerService.processAnswer(answerRequest);
    }

    @GetMapping("/sessions/{sessionId}/leaderboard")
    public ResponseEntity<LeaderBoardDto> getLeaderboard(@PathVariable String sessionId) {
        return gameSessionService.getLeaderBoard(sessionId);
    }
}
