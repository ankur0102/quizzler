package org.project.springbootstarter.pojo;

import lombok.Builder;
import lombok.Data;

@Data
public class StartRequestDto {
    private QuizDto quiz;
    private Long playerId;
}
