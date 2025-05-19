package org.project.springbootstarter.repository;

import org.project.springbootstarter.model.GameSession;
import org.project.springbootstarter.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByGameSession(GameSession gameSession);
}
