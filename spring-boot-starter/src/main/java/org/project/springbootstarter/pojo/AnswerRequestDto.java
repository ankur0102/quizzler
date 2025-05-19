package org.project.springbootstarter.pojo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AnswerRequestDto {
    private String answer;
    private Long playerId;
    private Long questionId;
    private Long submittedAt;
    private String sessionId;
}
