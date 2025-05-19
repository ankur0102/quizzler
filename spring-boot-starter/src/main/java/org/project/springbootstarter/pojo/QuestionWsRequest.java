package org.project.springbootstarter.pojo;

import lombok.Builder;
import lombok.Data;
import org.project.springbootstarter.model.Question;

@Data
@Builder
public class QuestionWsRequest {
    private Question question;
    private Long endTime;
    private Integer currentIndex;
    private Integer totalQuestions;
    private Boolean redirectToLeaderBoard;
}
