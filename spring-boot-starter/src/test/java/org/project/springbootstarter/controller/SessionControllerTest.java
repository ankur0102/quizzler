package org.project.springbootstarter.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.springbootstarter.pojo.*;
import org.project.springbootstarter.service.AnswerServiceImpl;
import org.project.springbootstarter.service.GameSessionServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private SessionController sessionController;
    @Mock
    private GameSessionServiceImpl gameSessionService;
    @Mock
    private AnswerServiceImpl answerService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(sessionController).build();
    }

    @Test
    void testInitSession() throws Exception {
        SessionResponseDto sessionResponseDto = SessionResponseDto.builder()
                .sessionId("abc")
                .playerId(100L)
                .nickname("Player1")
                .build();
        Mockito.when(gameSessionService.createNewSession(any(SessionRequest.class))).thenReturn(sessionResponseDto);
        mockMvc.perform(post("/api-server/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickname\": \"Player1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value("abc"))
                .andExpect(jsonPath("$.playerId").value(100L))
                .andExpect(jsonPath("$.nickname").value("Player1"));
    }

    @Test
    void testStartGame() throws Exception {
        StartRequestDto startRequestDto = new StartRequestDto();
        Mockito.when(gameSessionService.startGame(anyString(), any(StartRequestDto.class)))
                .thenReturn(ResponseEntity.ok("SUCCESS"));
        mockMvc.perform(post("/api-server/v1/sessions/1/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("SUCCESS"));
    }

    @Test
    void testAddPlayer() throws Exception {
        JoinResponseDto joinResponseDto = JoinResponseDto.builder()
                .playerId(101L)
                .nickname("Player1")
                .sessionId("abc")
                .websocketEP("ws://localhost")
                .build();

        Mockito.when(gameSessionService.addPlayer(anyString(), any(JoinRequestDto.class))).thenReturn(ResponseEntity.ok(joinResponseDto));
        mockMvc.perform(post("/api-server/v1/sessions/1/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickname\": \"Player1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerId").value(101L))
                .andExpect(jsonPath("$.nickname").value("Player1"))
                .andExpect(jsonPath("$.sessionId").value("abc"))
                .andExpect(jsonPath("$.websocketEP").value("ws://localhost"));
    }

    @Test
    void testNextQuestion() throws Exception {
        NextQuestionRequestDto nextQuestionRequestDto = new NextQuestionRequestDto();
        Mockito.when(gameSessionService.incrementQuestion(anyString(), any(NextQuestionRequestDto.class)))
                .thenReturn(ResponseEntity.ok("SUCCESS"));
        mockMvc.perform(post("/api-server/v1/sessions/1/next")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("SUCCESS"));
    }

    @Test
    void testAddAnswer() throws Exception {
        Mockito.when(answerService.processAnswer(any(AnswerRequestDto.class))).thenReturn(ResponseEntity.ok("SUCCESS"));
        mockMvc.perform(post("/api-server/v1/sessions/1/answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("SUCCESS"));
    }

    @Test
    void testGetLeaderboard() throws Exception {
        LeaderBoardDto leaderBoardDto = new LeaderBoardDto(new ArrayList<>());
        Mockito.when(gameSessionService.getLeaderBoard(anyString())).thenReturn(ResponseEntity.ok(leaderBoardDto));
        mockMvc.perform(get("/api-server/v1/sessions/1/leaderboard")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
