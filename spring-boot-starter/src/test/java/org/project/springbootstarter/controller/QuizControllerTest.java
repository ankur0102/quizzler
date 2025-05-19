package org.project.springbootstarter.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.springbootstarter.model.Quiz;
import org.project.springbootstarter.pojo.QuizDto;
import org.project.springbootstarter.service.QuestionServiceImpl;
import org.project.springbootstarter.service.QuizServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class QuizControllerTest {

    private MockMvc mockMvc;
    @InjectMocks
    private QuizController quizController;
    @Mock
    private QuizServiceImpl quizService;
    @Mock
    private QuestionServiceImpl questionService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(quizController).build();
    }

    @Test
    void testSaveQuiz() throws Exception {
        Quiz quiz = Quiz.builder().id(1L).title("Sample Quiz").build();
        QuizDto quizDto = QuizDto.builder().id(1L).title("Sample Quiz").build();
        Mockito.when(quizService.save(any(Quiz.class))).thenReturn(ResponseEntity.ok(quizDto));
        mockMvc.perform(post("/api-server/v1/quizzes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"title\": \"Sample Quiz\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Sample Quiz"));
    }

    @Test
    void testGetQuiz() throws Exception {
        Quiz quiz = Quiz.builder().id(1L).title("Sample Quiz").build();
        Mockito.when(quizService.findById(anyLong())).thenReturn(Optional.of(quiz));
        mockMvc.perform(get("/api-server/v1/quizzes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Sample Quiz"));
    }
}