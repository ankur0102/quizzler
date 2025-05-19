package org.project.springbootstarter.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class SessionResponseDto {
    private Long playerId;
    private String nickname;
    private List<QuizDto> quizzes;
    private String sessionId;
}