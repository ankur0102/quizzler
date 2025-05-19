package org.project.springbootstarter.pojo;

import lombok.Builder;
import lombok.Data;
import org.project.springbootstarter.model.Quiz;

@Data
@Builder
public class StartResponseDto {
    private Quiz quiz;
}
