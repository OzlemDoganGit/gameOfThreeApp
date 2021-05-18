package com.takeaway.gameofthree.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.InjectMocks;

import com.takeaway.gameofthree.domain.entity.Player;
import com.takeaway.gameofthree.enums.PlayerStatus;

public class PlayerTest {

    @InjectMocks
    Player player;

    @Test
    public void testBuildPlayer() {
        Player player1 = Player.builder().name("xx").playerStatus(PlayerStatus.CONNECTED).build();
        assertNotNull(player1);
    }
}
