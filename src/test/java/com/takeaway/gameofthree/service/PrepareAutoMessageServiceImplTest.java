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
import com.takeaway.gameofthree.enums.PlayType;
import com.takeaway.gameofthree.enums.PlayerStatus;
import com.takeaway.gameofthree.message.GameMessage;
import com.takeaway.gameofthree.service.impl.PrepareAutoMessageServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
public class PrepareAutoMessageServiceImplTest {

    @Mock
    FindPlayerPlaceService findPlayerService;

    @Mock
    GameOfThreeCommandService gameOfThreeCommandService;

    @Mock
    private BuildGameMessageService buildGameMessageService;

    @InjectMocks
    PrepareAutoMessageServiceImpl prepareAutoMessageServiceImpl;

    @Test
    public void testPrepareAutoMessages() {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(5L).gameId(1L)
                .gameStatus(GameStatus.ON_GOING).move(1).number(1).playerId(6L)
                .playType(PlayType.AUTOMATIC_PLAY).build();
        Player player1 = Player.builder().name("name").playerStatus(PlayerStatus.CONNECTED).id(6L).build();
        Player player2 = Player.builder().name("f").playerStatus(PlayerStatus.CONNECTED).id(5L).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(1).operator(ArithmeticOperator.DIVIDE).startedNumber(1)
                .updatedNumber(1).build();
        Game game = Game.builder().gameStatus(GameStatus.ON_GOING).playerList(playerList).creationTime(now)
                .id(1L).point(point).build();

        Mockito.when(gameOfThreeCommandService.updateStatusOfAutoGame(Mockito.any(), Mockito.any())).thenReturn(game);
        Integer placeOfPlayer = 0;
        Mockito.when(findPlayerService.findThePlaceOfStartingPlayer(Mockito.any(), Mockito.any())).thenReturn(placeOfPlayer);
        Integer nextPlayerPlace = 1;
        Mockito.when(findPlayerService.findThePlaceOfSecondPlayer(Mockito.anyInt())).thenReturn(nextPlayerPlace);        
        Mockito.when(gameOfThreeCommandService.updatePointOfAutoGame(Mockito.any(), Mockito.any())).thenReturn(game);
        
        Mockito.when(buildGameMessageService.buildAutoGameMessage(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(gameMessage);
        
        prepareAutoMessageServiceImpl.prepareAutoMessages(game, gameMessage);

    }


}
