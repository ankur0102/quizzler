package org.project.springbootstarter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.springbootstarter.model.GameSession;
import org.project.springbootstarter.model.Player;
import org.project.springbootstarter.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceImplTest {

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Test
    void testCreateNewPlayerWithNicknameAndGameSession() {
        String nickname = "Player1";
        GameSession gameSession = GameSession.builder().id("abc").status("NEW").build();
        Player savedPlayer = Player.builder().id(1L).nickname(nickname).gameSession(gameSession).build();
        Mockito.when(playerRepository.save(any(Player.class))).thenReturn(savedPlayer);
        Player result = playerService.createNewPlayer(nickname, gameSession);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("Player1", result.getNickname());
        Assertions.assertEquals(gameSession, result.getGameSession());
    }

    @Test
    void testCreateNewPlayerWithPlayerObject() {
        Player player = Player.builder().nickname("Player2").gameSession(GameSession.builder().id("abc2").build()).build();
        Player savedPlayer = Player.builder().id(2L).nickname("Player2").build();
        Mockito.when(playerRepository.save(player)).thenReturn(savedPlayer);
        Player result = playerService.createNewPlayer(player);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2L, result.getId());
        Assertions.assertEquals("Player2", result.getNickname());
    }

    @Test
    void testGetPlayerById() {
        Long playerId = 1L;
        Player player = Player.builder().id(playerId).nickname("Player1").build();
        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        Optional<Player> result = playerService.getPlayerById(playerId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Player1", result.get().getNickname());
    }

    @Test
    void testGetPlayerByIdNotFound() {
        Long playerId = 1L;
        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.empty());
        Optional<Player> result = playerService.getPlayerById(playerId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testGetPlayersByGameSession() {
        GameSession gameSession = GameSession.builder().id("abc").status("NEW").build();
        List<Player> players = new ArrayList<>();
        players.add(Player.builder().id(1L).nickname("Player1").build());
        players.add(Player.builder().id(2L).nickname("Player2").build());
        Mockito.when(playerRepository.findByGameSession(gameSession)).thenReturn(players);
        List<Player> result = playerService.getPlayersByGameSession(gameSession);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Player1", result.get(0).getNickname());
        Assertions.assertEquals("Player2", result.get(1).getNickname());
    }
}