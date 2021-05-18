package com.takeaway.gameofthree.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.domain.entity.Player;
import com.takeaway.gameofthree.domain.entity.Point;
import com.takeaway.gameofthree.enums.ArithmeticOperator;
import com.takeaway.gameofthree.enums.GameStatus;
import com.takeaway.gameofthree.enums.PlayType;
import com.takeaway.gameofthree.enums.PlayerStatus;

@RunWith(SpringJUnit4ClassRunner.class)
public class GameTest {

    @InjectMocks
    Game game;

    @Test
    public void testBuildGame() {
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        List<Player> playerList = new ArrayList<>();
        Player player1 = Player.builder().name("xx").playerStatus(PlayerStatus.CONNECTED).build();
        Player player2 = Player.builder().name("yy").playerStatus(PlayerStatus.CONNECTED).build();
        playerList.add(player1);
        playerList.add(player2);
        Game createdGame = Game.builder().gameStatus(GameStatus.PAIRED).point(point).creationTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now()).playType(PlayType.MANUAL_PLAY).playerList(playerList).id(1L).build();
        assertNotNull(createdGame);
    }

    @Test
    public void testSetPoint() {
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        game.setPoint(point);
        assertEquals(point, game.getPoint());
    }

    @Test
    public void testSetPlayerList() {
        List<Player> playerList = new ArrayList<>();
        Player player1 = Player.builder().name("xx").playerStatus(PlayerStatus.CONNECTED).build();
        Player player2 = Player.builder().name("yy").playerStatus(PlayerStatus.CONNECTED).build();
        playerList.add(player1);
        playerList.add(player2);
        game.setPlayerList(playerList);
        assertEquals(playerList, game.getPlayerList());
    }

    @Test
    public void testSetGameStatus() {
        game.setGameStatus(GameStatus.FINALIZED);
        assertEquals(GameStatus.FINALIZED, game.getGameStatus());
    }

    @Test
    public void testGetCreationTime() {
        game.setCreationTime(LocalDateTime.now());
        assertEquals(LocalDateTime.now(), game.getCreationTime());
    }

    @Test
    public void testGetUpdateTime() {
        game.setUpdateTime(LocalDateTime.now());
        assertEquals(LocalDateTime.now(), game.getUpdateTime());
    }

    @Test
    public void testGetId() {
        game.setId(1L);
        assertEquals(1L, game.getId());
    }

}
