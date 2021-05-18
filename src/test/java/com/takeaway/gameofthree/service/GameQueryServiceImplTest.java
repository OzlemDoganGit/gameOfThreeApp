package com.takeaway.gameofthree.service;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.domain.entity.Player;
import com.takeaway.gameofthree.domain.entity.Point;
import com.takeaway.gameofthree.enums.ArithmeticOperator;
import com.takeaway.gameofthree.enums.GameStatus;
import com.takeaway.gameofthree.enums.PlayerStatus;
import com.takeaway.gameofthree.repository.GameRepository;
import com.takeaway.gameofthree.service.impl.GameOfThreeQueryServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
public class GameQueryServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameOfThreeQueryServiceImpl gameOfThreeQueryServiceImpl;

    @Test
    public void testRetrieveWaitingGames() {

        Player player = Player.builder().name("").playerStatus(PlayerStatus.CONNECTED).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        Game game = Game.builder().gameStatus(GameStatus.WAITING_FOR_PLAYER).playerList(playerList).creationTime(now)
                .point(point).build();
        gameRepository.save(game);
        gameOfThreeQueryServiceImpl.retriveWaitingGames();

    }

    @Test
    public void testRetrieveTheGameById() {
        Player player = Player.builder().name("").playerStatus(PlayerStatus.CONNECTED).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        Game game = Game.builder().gameStatus(GameStatus.WAITING_FOR_PLAYER).playerList(playerList).creationTime(now)
                .point(point).build();

        Mockito.when(gameRepository.findById(Mockito.anyLong())).thenReturn(Mockito.any());
        gameOfThreeQueryServiceImpl.retrieveTheGameById(game.getId());
    }
}
