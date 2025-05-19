package org.project.springbootstarter.pojo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PlayerScore {
    private Long score;
    private String playerName;
    private Long playerId;
}
