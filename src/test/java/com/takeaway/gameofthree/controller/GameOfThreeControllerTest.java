package com.takeaway.gameofthree.controller;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.takeaway.gameofthree.GameOfThreeApplication;
import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.domain.entity.Player;
import com.takeaway.gameofthree.domain.entity.Point;
import com.takeaway.gameofthree.enums.ArithmeticOperator;
import com.takeaway.gameofthree.enums.GameStatus;
import com.takeaway.gameofthree.enums.PlayType;
import com.takeaway.gameofthree.enums.PlayerStatus;
import com.takeaway.gameofthree.message.GameMessage;
import com.takeaway.gameofthree.service.BuildGameMessageService;
import com.takeaway.gameofthree.service.FindPlayerPlaceService;
import com.takeaway.gameofthree.service.GameOfThreeCommandService;
import com.takeaway.gameofthree.service.GameOfThreeQueryService;
import com.takeaway.gameofthree.service.PrepareAutoMessageService;

@RunWith(SpringJUnit4ClassRunner.class)
public class GameOfThreeControllerTest {

    @Mock
    private GameOfThreeCommandService gameOfThreeCommandService;

    @Mock
    private GameOfThreeQueryService gameOfThreeQueryService;

    @Mock
    private FindPlayerPlaceService findPlayerService;

    @Mock
    private BuildGameMessageService buildGameMessageService;

    @Mock
    private PrepareAutoMessageService prepareAutoMessageService;

    private static String PATH = "/";

    private static final String TOPICPREFIX = "/topic/gameOfThree/";

    private MockMvc mockMvc;

    @InjectMocks
    private GameOfThreeController gameOfThreeController;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(gameOfThreeController).build();
    }

    @Test
    public void testJoinToTheGame() {
        List<Game> waitingGameList = gameOfThreeQueryService.retriveWaitingGames();
        gameOfThreeCommandService.createOrJoinToTheGame(waitingGameList);
        ResponseEntity<Game> actual = gameOfThreeController.joinToTheGame();
        Mockito.verify(gameOfThreeQueryService, Mockito.times(2)).retriveWaitingGames();
        Mockito.verify(gameOfThreeCommandService, Mockito.times(2)).createOrJoinToTheGame(waitingGameList);
        Assert.assertNotNull(actual);
    }

    @Test
    public void testManualPlayGame() {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(5L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(6L).playType(PlayType.AUTOMATIC_PLAY).build();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        List<Player> playerList = new ArrayList<>();
        Player player1 = Player.builder().name("xx").playerStatus(PlayerStatus.CONNECTED).build();
        Player player2 = Player.builder().name("yy").playerStatus(PlayerStatus.CONNECTED).build();
        playerList.add(player1);
        playerList.add(player2);
        Game createdGame = Game.builder().gameStatus(GameStatus.PAIRED).point(point).creationTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now()).playType(PlayType.MANUAL_PLAY).playerList(playerList).id(1L).build();
        Optional<Game> optGame = Optional.of(createdGame);
        assertNotNull(optGame);
        Mockito.when(gameOfThreeQueryService.retrieveTheGameById(Mockito.anyLong())).thenReturn(optGame);

        Mockito.when(gameOfThreeCommandService.updateTheGameStatus(Mockito.any(), Mockito.any()))
                .thenReturn(createdGame);

        Mockito.when(buildGameMessageService.buildManualGameMessage(Mockito.any(), Mockito.any()))
                .thenReturn(gameMessage);

        this.messagingTemplate.convertAndSend(TOPICPREFIX.concat(String.valueOf(createdGame.getId())), gameMessage);
        Mockito.verify(messagingTemplate, Mockito.times(1))
                .convertAndSend(TOPICPREFIX.concat(String.valueOf(createdGame.getId())), gameMessage);

        gameOfThreeController.manualPlayGame(gameMessage);

    }

    @Test
    public void testCalculateNumber() {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(5L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(6L).playType(PlayType.AUTOMATIC_PLAY).build();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        List<Player> playerList = new ArrayList<>();
        Player player1 = Player.builder().name("xx").playerStatus(PlayerStatus.CONNECTED).build();
        Player player2 = Player.builder().name("yy").playerStatus(PlayerStatus.CONNECTED).build();
        playerList.add(player1);
        playerList.add(player2);
        Game createdGame = Game.builder().gameStatus(GameStatus.PAIRED).point(point).creationTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now()).playType(PlayType.MANUAL_PLAY).playerList(playerList).id(1L).build();
        Optional<Game> optGame = Optional.of(createdGame);
        assertNotNull(optGame);

        Mockito.when(gameOfThreeQueryService.retrieveTheGameById(Mockito.anyLong())).thenReturn(optGame);
        Mockito.when(gameOfThreeCommandService.updateTheGame(Mockito.any(), Mockito.any())).thenReturn(createdGame);
        Mockito.when(buildGameMessageService.buildManualGameMessage(Mockito.any(), Mockito.any()))
                .thenReturn(gameMessage);

        messagingTemplate.convertAndSend(TOPICPREFIX.concat(String.valueOf(createdGame.getId())), gameMessage);
        Mockito.verify(messagingTemplate, Mockito.times(1)).convertAndSend(TOPICPREFIX + createdGame.getId(),
                gameMessage);
        gameOfThreeController.calculateNumber(gameMessage);

    }

    @Test
    public void testAutoPlayGame() {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(5L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(6L).playType(PlayType.AUTOMATIC_PLAY).build();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        List<Player> playerList = new ArrayList<>();
        Player player1 = Player.builder().name("xx").playerStatus(PlayerStatus.CONNECTED).build();
        Player player2 = Player.builder().name("yy").playerStatus(PlayerStatus.CONNECTED).build();
        playerList.add(player1);
        playerList.add(player2);
        Game createdGame = Game.builder().gameStatus(GameStatus.PAIRED).point(point).creationTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now()).playType(PlayType.MANUAL_PLAY).playerList(playerList).id(1L).build();
        Optional<Game> optGame = Optional.of(createdGame);
        assertNotNull(optGame);
        Mockito.when(gameOfThreeQueryService.retrieveTheGameById(Mockito.anyLong())).thenReturn(optGame);

        List<GameMessage> gameMessageList = new ArrayList<GameMessage>();
        gameMessageList.add(gameMessage);
        gameMessageList.add(gameMessage);
        Mockito.when(prepareAutoMessageService.prepareAutoMessages(Mockito.any(), Mockito.any()))
                .thenReturn(gameMessageList);

        for (GameMessage sentGameMessage : gameMessageList) {
            messagingTemplate.convertAndSend(TOPICPREFIX.concat(String.valueOf(gameMessage.getGameId())),
                    sentGameMessage);
        }
        Mockito.verify(messagingTemplate, Mockito.times(2)).convertAndSend(TOPICPREFIX + gameMessage.getGameId(),
                gameMessage);
        List<GameMessage> newGameMessageList = new ArrayList<>();
        if (newGameMessageList.isEmpty()) {
            Integer placeOfPlayer = findPlayerService.findThePlaceOfStartingPlayer(createdGame, gameMessage);
            Mockito.when(findPlayerService.findThePlaceOfStartingPlayer(createdGame, gameMessage))
                    .thenReturn(placeOfPlayer);
            Mockito.verify(findPlayerService, Mockito.times(1)).findThePlaceOfStartingPlayer(createdGame, gameMessage);

            Integer nextPlayerPlace = findPlayerService.findThePlaceOfSecondPlayer(placeOfPlayer);
            Mockito.when(findPlayerService.findThePlaceOfSecondPlayer(placeOfPlayer)).thenReturn(placeOfPlayer);
            Mockito.verify(findPlayerService, Mockito.times(1)).findThePlaceOfSecondPlayer(nextPlayerPlace);

            GameMessage sentGameMessage = buildGameMessageService.buildAutoGameMessage(createdGame, placeOfPlayer,
                    nextPlayerPlace);
            messagingTemplate.convertAndSend(TOPICPREFIX.concat(String.valueOf(createdGame.getId())), sentGameMessage);
        }

        gameOfThreeController.autoPlayGame(gameMessage);

    }

    @Test
    public void autoMessageListEmpty() {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(5L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(6L).playType(PlayType.AUTOMATIC_PLAY).build();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        List<Player> playerList = new ArrayList<>();
        Player player1 = Player.builder().name("xx").playerStatus(PlayerStatus.CONNECTED).build();
        Player player2 = Player.builder().name("yy").playerStatus(PlayerStatus.CONNECTED).build();
        playerList.add(player1);
        playerList.add(player2);
        Game createdGame = Game.builder().gameStatus(GameStatus.PAIRED).point(point).creationTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now()).playType(PlayType.MANUAL_PLAY).playerList(playerList).id(1L).build();
        Optional<Game> optGame = Optional.of(createdGame);
        assertNotNull(optGame);
        Mockito.when(gameOfThreeQueryService.retrieveTheGameById(Mockito.anyLong())).thenReturn(optGame);
        new ArrayList<GameMessage>();
        gameOfThreeController.autoPlayGame(gameMessage);

    }

    @Test
    public void testIDNotExistManualPlayGame() {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(5L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(6L).playType(PlayType.AUTOMATIC_PLAY).build();

        gameOfThreeQueryService.retrieveTheGameById(gameMessage.getGameId());
        Mockito.verify(gameOfThreeQueryService, Mockito.times(1)).retrieveTheGameById(gameMessage.getGameId());

        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        List<Player> playerList = new ArrayList<>();
        Player player1 = Player.builder().name("xx").playerStatus(PlayerStatus.CONNECTED).build();
        Player player2 = Player.builder().name("yy").playerStatus(PlayerStatus.CONNECTED).build();
        playerList.add(player1);
        playerList.add(player2);
        Game createdGame = Game.builder().gameStatus(GameStatus.PAIRED).point(point).creationTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now()).playType(PlayType.MANUAL_PLAY).playerList(playerList).id(1L).build();
        Optional<Game> optGame = Optional.of(createdGame);
        assertNotNull(optGame);

        Game updatedGame = gameOfThreeCommandService.updateTheGameStatus(optGame.get(), gameMessage);
        Mockito.when(gameOfThreeCommandService.updateTheGameStatus(optGame.get(), gameMessage)).thenReturn(updatedGame);
        Mockito.verify(gameOfThreeCommandService, Mockito.times(1)).updateTheGameStatus(createdGame, gameMessage);

        buildGameMessageService.buildManualGameMessage(createdGame, gameMessage);
        Mockito.when(buildGameMessageService.buildManualGameMessage(createdGame, gameMessage)).thenReturn(gameMessage);
        Mockito.verify(buildGameMessageService, Mockito.times(1)).buildManualGameMessage(createdGame, gameMessage);

        this.messagingTemplate.convertAndSend(TOPICPREFIX.concat(String.valueOf(createdGame.getId())), gameMessage);
        Mockito.verify(messagingTemplate, Mockito.times(1))
                .convertAndSend(TOPICPREFIX.concat(String.valueOf(createdGame.getId())), gameMessage);

        gameOfThreeController.manualPlayGame(gameMessage);

    }

    @Test
    public void testIDNotExistCalculateNumber() {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(5L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(6L).playType(PlayType.AUTOMATIC_PLAY).build();

        Optional<Game> game = gameOfThreeQueryService.retrieveTheGameById(gameMessage.getGameId());
        Mockito.when(gameOfThreeQueryService.retrieveTheGameById(Mockito.anyLong())).thenReturn(game);
        Mockito.verify(gameOfThreeQueryService, Mockito.times(1)).retrieveTheGameById(gameMessage.getGameId());

        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        List<Player> playerList = new ArrayList<>();
        Player player1 = Player.builder().name("xx").playerStatus(PlayerStatus.CONNECTED).build();
        Player player2 = Player.builder().name("yy").playerStatus(PlayerStatus.CONNECTED).build();
        playerList.add(player1);
        playerList.add(player2);
        Game createdGame = Game.builder().gameStatus(GameStatus.PAIRED).point(point).creationTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now()).playType(PlayType.MANUAL_PLAY).playerList(playerList).id(1L).build();
        Optional<Game> optGame = Optional.of(createdGame);
        assertNotNull(optGame);

        Game updatedGame = gameOfThreeCommandService.updateTheGame(createdGame, gameMessage);
        Mockito.when(gameOfThreeCommandService.updateTheGame(createdGame, gameMessage)).thenReturn(updatedGame);
        Mockito.verify(gameOfThreeCommandService, Mockito.times(1)).updateTheGame(createdGame, gameMessage);

        buildGameMessageService.buildManualGameMessage(createdGame, gameMessage);
        Mockito.when(buildGameMessageService.buildManualGameMessage(createdGame, gameMessage)).thenReturn(gameMessage);
        Mockito.verify(buildGameMessageService, Mockito.times(1)).buildManualGameMessage(createdGame, gameMessage);
        messagingTemplate.convertAndSend(TOPICPREFIX.concat(String.valueOf(createdGame.getId())), gameMessage);
        Mockito.verify(messagingTemplate, Mockito.times(1)).convertAndSend(TOPICPREFIX + createdGame.getId(),
                gameMessage);

        gameOfThreeController.calculateNumber(gameMessage);

    }

    @Test
    public void testIDNotExistAutoPlayGame() {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(5L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(6L).playType(PlayType.AUTOMATIC_PLAY).build();

        Optional<Game> game = gameOfThreeQueryService.retrieveTheGameById(gameMessage.getGameId());
        Mockito.when(gameOfThreeQueryService.retrieveTheGameById(Mockito.anyLong())).thenReturn(game);
        Mockito.verify(gameOfThreeQueryService, Mockito.times(1)).retrieveTheGameById(gameMessage.getGameId());

        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        List<Player> playerList = new ArrayList<>();
        Player player1 = Player.builder().name("xx").playerStatus(PlayerStatus.CONNECTED).build();
        Player player2 = Player.builder().name("yy").playerStatus(PlayerStatus.CONNECTED).build();
        playerList.add(player1);
        playerList.add(player2);
        Game createdGame = Game.builder().gameStatus(GameStatus.PAIRED).point(point).creationTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now()).playType(PlayType.MANUAL_PLAY).playerList(playerList).id(1L).build();
        Optional<Game> optGame = Optional.of(createdGame);
        assertNotNull(optGame);

        List<GameMessage> gameMessageList = prepareAutoMessageService.prepareAutoMessages(createdGame, gameMessage);

        Mockito.when(prepareAutoMessageService.prepareAutoMessages(createdGame, gameMessage))
                .thenReturn(gameMessageList);
        Mockito.verify(prepareAutoMessageService, Mockito.times(1)).prepareAutoMessages(createdGame, gameMessage);

        gameMessageList.add(gameMessage);

        for (GameMessage sentGameMessage : gameMessageList) {
            messagingTemplate.convertAndSend(TOPICPREFIX.concat(String.valueOf(gameMessage.getGameId())),
                    sentGameMessage);
        }
        Mockito.verify(messagingTemplate, Mockito.times(1)).convertAndSend(TOPICPREFIX + gameMessage.getGameId(),
                gameMessage);
        List<GameMessage> newGameMessageList = new ArrayList<>();
        if (newGameMessageList.isEmpty()) {
            Integer placeOfPlayer = findPlayerService.findThePlaceOfStartingPlayer(createdGame, gameMessage);
            Mockito.when(findPlayerService.findThePlaceOfStartingPlayer(createdGame, gameMessage))
                    .thenReturn(placeOfPlayer);
            Mockito.verify(findPlayerService, Mockito.times(1)).findThePlaceOfStartingPlayer(createdGame, gameMessage);

            Integer nextPlayerPlace = findPlayerService.findThePlaceOfSecondPlayer(placeOfPlayer);
            Mockito.when(findPlayerService.findThePlaceOfSecondPlayer(placeOfPlayer)).thenReturn(placeOfPlayer);
            Mockito.verify(findPlayerService, Mockito.times(1)).findThePlaceOfSecondPlayer(nextPlayerPlace);

            GameMessage sentGameMessage = buildGameMessageService.buildAutoGameMessage(createdGame, placeOfPlayer,
                    nextPlayerPlace);
            messagingTemplate.convertAndSend(TOPICPREFIX.concat(String.valueOf(createdGame.getId())), sentGameMessage);
        }

        gameOfThreeController.autoPlayGame(gameMessage);

    }

}
