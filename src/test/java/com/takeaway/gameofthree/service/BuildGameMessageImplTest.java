package com.takeaway.gameofthree.service;

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
import com.takeaway.gameofthree.message.GameMessage;
import com.takeaway.gameofthree.service.impl.BuildGameMessageServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
public class BuildGameMessageImplTest {

    @InjectMocks
    BuildGameMessageServiceImpl buildGameMessageServiceImpl;

    @Test
    public void testBuildAutoGameMessage_STARTED() // TWO PLAYER
    {
        Player player = Player.builder().name("d").playerStatus(PlayerStatus.CONNECTED).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        playerList.add(player);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).adjustedNumber(1).build();
        Game game = Game.builder().gameStatus(GameStatus.STARTED).playerList(playerList).creationTime(now).point(point)
                .build();
        buildGameMessageServiceImpl.buildAutoGameMessage(game, 0, 1);
    }

    @Test
    public void testBuildAutoGameMessage_WAITING_FOR_PLAYER() // ONE PLAYER
    {
        Player player = Player.builder().name("d").playerStatus(PlayerStatus.CONNECTED).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        Game game = Game.builder().gameStatus(GameStatus.WAITING_FOR_PLAYER).playerList(playerList).creationTime(now)
                .point(point).build();
        buildGameMessageServiceImpl.buildAutoGameMessage(game, 0, 1);
    }

    @Test
    public void testBuildAutoGameMessage_OTHER_STATES() // TWO PLAYER
    {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(1L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(2L).playType(PlayType.MANUAL_PLAY).build();
        Player player = Player.builder().name("d").playerStatus(PlayerStatus.CONNECTED).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        playerList.add(player);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).adjustedNumber(2).build();
        Game game = Game.builder().gameStatus(GameStatus.ON_GOING).playerList(playerList).creationTime(now).point(point)
                .build();
        buildGameMessageServiceImpl.buildAutoGameMessage(game, 0, 1);
    }

    @Test
    public void testBuildManualGameMessage_STARTED() // TWO PLAYER
    {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(1L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(2L).playType(PlayType.MANUAL_PLAY).build();
        Player player = Player.builder().name("d").playerStatus(PlayerStatus.CONNECTED).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        playerList.add(player);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).adjustedNumber(1).build();
        Game game = Game.builder().gameStatus(GameStatus.STARTED).playerList(playerList).creationTime(now).point(point)
                .build();
        buildGameMessageServiceImpl.buildManualGameMessage(game, gameMessage);
    }

    @Test
    public void testBuildManualGameMessage_WAITING_FOR_PLAYER() // ONE PLAYER
    {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(1L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(2L).playType(PlayType.MANUAL_PLAY).build();
        Player player = Player.builder().name("d").playerStatus(PlayerStatus.CONNECTED).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        Game game = Game.builder().gameStatus(GameStatus.WAITING_FOR_PLAYER).playerList(playerList).creationTime(now)
                .point(point).build();
        buildGameMessageServiceImpl.buildManualGameMessage(game, gameMessage);
    }

    @Test
    public void testBuildManualGameMessage_OTHER_STATES() // TWO PLAYER
    {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(1L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(2L).playType(PlayType.MANUAL_PLAY).build();
        Player player = Player.builder().name("d").playerStatus(PlayerStatus.CONNECTED).id(2L).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        playerList.add(player);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).adjustedNumber(2).build();
        Game game = Game.builder().gameStatus(GameStatus.ON_GOING).playerList(playerList).creationTime(now).point(point)
                .build();
        buildGameMessageServiceImpl.buildManualGameMessage(game, gameMessage);
    }
}
