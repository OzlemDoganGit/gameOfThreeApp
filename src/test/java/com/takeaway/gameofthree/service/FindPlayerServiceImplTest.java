package com.takeaway.gameofthree.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import com.takeaway.gameofthree.service.impl.FindPlayerServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
public class FindPlayerServiceImplTest {

    @InjectMocks
    FindPlayerServiceImpl findPlayerServiceImpl;

    @Test
    public void testFindThePlaceOfStartingPlayer() {
        GameMessage gameMessage = GameMessage.builder().from("f").fromId(1L).gameId(6L).gameStatus(GameStatus.ON_GOING)
                .move(1).number(5).playerId(2L).playType(PlayType.AUTOMATIC_PLAY).build();
        Player player1 = Player.builder().name("").playerStatus(PlayerStatus.CONNECTED).id(1L).build();
        Player player2 = Player.builder().name("").playerStatus(PlayerStatus.CONNECTED).id(2L).build();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        assertNotNull(playerList);
        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        Game game = Game.builder().gameStatus(GameStatus.NOT_STARTED).playerList(playerList).creationTime(now)
                .point(point).build();

        findPlayerServiceImpl.findThePlaceOfStartingPlayer(game, gameMessage);

    }

    @Test
    public void findThePlaceOfSecondPlayer() {
        Integer placeOfPlayer = 0;
        findPlayerServiceImpl.findThePlaceOfSecondPlayer(placeOfPlayer);
        assertTrue(true);
    }

}
