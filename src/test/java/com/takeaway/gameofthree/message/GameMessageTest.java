package com.takeaway.gameofthree.message;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.takeaway.gameofthree.enums.GameStatus;
import com.takeaway.gameofthree.enums.PlayType;

@RunWith(SpringJUnit4ClassRunner.class)
public class GameMessageTest {

    @InjectMocks
    GameMessage gameMessage;

    @Test
    public void testBuildGameMessage() {
        GameMessage gameMessage = GameMessage.builder().from("").fromId(4L).gameId(6L).gameStatus(GameStatus.FINALIZED)
                .move(5).number(5).playerId(6L).playType(PlayType.MANUAL_PLAY).build();
        assertNotNull(gameMessage);
    }

}
