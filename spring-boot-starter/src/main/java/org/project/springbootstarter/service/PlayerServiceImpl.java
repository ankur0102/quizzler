package org.project.springbootstarter.service;

import org.project.springbootstarter.model.GameSession;
import org.project.springbootstarter.model.Player;
import org.project.springbootstarter.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public Player createNewPlayer(String nickname, GameSession gameSession) {
        return playerRepository.save(Player.builder().nickname(nickname).gameSession(gameSession).build());
    }

    public Player createNewPlayer(Player player) {
        return playerRepository.save(player);
    }

    public Optional<Player> getPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    public List<Player> getPlayersByGameSession(GameSession gameSession) {
        return playerRepository.findByGameSession(gameSession);
    }
}
