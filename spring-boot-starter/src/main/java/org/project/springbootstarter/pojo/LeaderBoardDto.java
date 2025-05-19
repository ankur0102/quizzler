package org.project.springbootstarter.pojo;

import lombok.Data;

import java.util.List;
@Data
public class LeaderBoardDto {
    List<PlayerScore> playerScores;

    public LeaderBoardDto(List<PlayerScore> playerScores) {
        this.playerScores = playerScores;
    }
}