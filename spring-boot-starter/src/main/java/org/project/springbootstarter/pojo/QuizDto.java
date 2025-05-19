package org.project.springbootstarter.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizDto {
    private Long id;
    private String title;
    private Long createdAt;
    private String metadata;
}
