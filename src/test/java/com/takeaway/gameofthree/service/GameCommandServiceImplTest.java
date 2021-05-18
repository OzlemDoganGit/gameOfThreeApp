package com.takeaway.gameofthree.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import com.takeaway.gameofthree.enums.PlayType;
import com.takeaway.gameofthree.enums.PlayerStatus;
import com.takeaway.gameofthree.enums.ValidationStatus;
import com.takeaway.gameofthree.message.GameMessage;
import com.takeaway.gameofthree.repository.GameRepository;
import com.takeaway.gameofthree.service.impl.GameOfThreeCommandServiceImpl;
import com.takeaway.gameofthree.utils.ValidatedNumber;

@RunWith(SpringJUnit4ClassRunner.class)
public class GameCommandServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private FindPlayerPlaceService findPlayerService;

    @Mock
    private NumberGeneratorService numberGeneratorService;

    @InjectMocks
    private GameOfThreeCommandServiceImpl gameOfThreeCommandService;

    private static final String PLAYER = "PLAYER-";

    @Test
    public void testJoinToTheGame() {
        List<Game> gameList = new ArrayList<Game>();
        Player player = Player.builder().name(PLAYER).playerStatus(PlayerStatus.CONNECTED).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        Game game = Game.builder().gameStatus(GameStatus.NOT_STARTED).playerList(playerList).creationTime(now)
                .point(point).build();
        gameList = new ArrayList<Game>();
        gameList.add(game);
        gameOfThreeCommandService.createOrJoinToTheGame(gameList);
    }

    @Test
    public void testCreateTheGame() {
        List<Game> gameList = new ArrayList<Game>();
        gameOfThreeCommandService.createOrJoinToTheGame(gameList);
        assertTrue(true);
    }

    @Test
    public void testUpdateTheGameStatusForOnePlayer() {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(5L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(6L).playType(PlayType.MANUAL_PLAY).build();
        Player player = Player.builder().name(PLAYER).playerStatus(PlayerStatus.CONNECTED).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        Game game = Game.builder().gameStatus(GameStatus.NOT_STARTED).playerList(playerList).creationTime(now)
                .point(point).build();
        gameOfThreeCommandService.updateTheGameStatus(game, gameMessage);
    }

    @Test
    public void testUpdateTheGameStatusForTwoPlayer() {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(5L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(6L).playType(PlayType.MANUAL_PLAY).build();
        Player player = Player.builder().name(PLAYER).playerStatus(PlayerStatus.CONNECTED).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        playerList.add(player);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        Game game = Game.builder().gameStatus(GameStatus.NOT_STARTED).playerList(playerList).creationTime(now)
                .point(point).build();
        gameOfThreeCommandService.updateTheGameStatus(game, gameMessage);
    }

    @Test
    public void testUpdateTheValidGame() {
        ValidatedNumber.builder().move(1).number(55).status(ValidationStatus.VALID).build();
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(5L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(6L).playType(PlayType.MANUAL_PLAY).build();
        Player player = Player.builder().name(PLAYER).playerStatus(PlayerStatus.CONNECTED).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        playerList.add(player);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        Game game = Game.builder().gameStatus(GameStatus.NOT_STARTED).playerList(playerList).creationTime(now)
                .point(point).build();
        gameOfThreeCommandService.updateTheGame(game, gameMessage);
    }

    @Test
    public void testUpdateNotValidGame() {
        ValidatedNumber.builder().move(1).number(55).status(ValidationStatus.NOT_VALID).build();
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(5L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(2).number(5).playerId(6L).playType(PlayType.MANUAL_PLAY).build();
        Player player = Player.builder().name(PLAYER).playerStatus(PlayerStatus.CONNECTED).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        playerList.add(player);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        Game game = Game.builder().gameStatus(GameStatus.ERROR).playerList(playerList).creationTime(now)
                .point(point).build();
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(game);
        gameOfThreeCommandService.updateTheGame(game, gameMessage);
    }

    @Test
    public void testUpdatePointOfAutoGame() {
        List<Player> playerList = new ArrayList<>();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        Game game = Game.builder().gameStatus(GameStatus.NOT_STARTED).playerList(playerList)
                .creationTime(LocalDateTime.now()).point(point).build();
        gameOfThreeCommandService.updatePointOfAutoGame(point, game);
        assertTrue(true);

    }

    @Test
    public void testUpdateStatusOfAutoGameForOnePlayer() {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(5L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(6L).playType(PlayType.AUTOMATIC_PLAY).build();
        Player player = Player.builder().name(PLAYER).playerStatus(PlayerStatus.CONNECTED).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        Game game = Game.builder().gameStatus(GameStatus.NOT_STARTED).playerList(playerList).creationTime(now)
                .point(point).build();
        gameOfThreeCommandService.updateStatusOfAutoGame(game, gameMessage);
    }
    
    @Test
    public void testUpdateStatusOfAutoGameForTwoPlayer() {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(5L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(6L).playType(PlayType.AUTOMATIC_PLAY).build();
        Player player = Player.builder().name(PLAYER).playerStatus(PlayerStatus.CONNECTED).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        playerList.add(player);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        Game game = Game.builder().gameStatus(GameStatus.NOT_STARTED).playerList(playerList).creationTime(now)
                .point(point).build();
        gameOfThreeCommandService.updateStatusOfAutoGame(game, gameMessage);
    }
}
