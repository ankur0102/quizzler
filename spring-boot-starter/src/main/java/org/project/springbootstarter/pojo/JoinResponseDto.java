package org.project.springbootstarter.pojo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JoinResponseDto {
    private String sessionId;
    private String nickname;
    private Long playerId;
    private String websocketEP;
}
