package org.project.springbootstarter.service;

import org.project.springbootstarter.pojo.*;
import org.springframework.http.ResponseEntity;

public interface GameSessionService {
    SessionResponseDto createNewSession(SessionRequest sessionRequest);
    ResponseEntity<String> startGame(String sessionId, StartRequestDto startRequestDto);
    ResponseEntity<String> incrementQuestion(String sessionId, NextQuestionRequestDto nextQuestionRequestDto);
    ResponseEntity<JoinResponseDto> addPlayer(String sessionId, JoinRequestDto joinRequestDto);

}
