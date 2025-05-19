package org.project.springbootstarter.service;

import org.project.springbootstarter.model.GameSession;
import org.project.springbootstarter.model.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerService {
    Player createNewPlayer(String nickname, GameSession gameSession);
    Player createNewPlayer(Player player);
    Optional<Player> getPlayerById(Long id);
    List<Player> getPlayersByGameSession(GameSession gameSession);
}
