package com.takeaway.gameofthree.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.takeaway.gameofthree.domain.entity.Player;
import com.takeaway.gameofthree.enums.PlayerStatus;

@RunWith(SpringJUnit4ClassRunner.class)
public class PlayerTest {

    @InjectMocks
    Player player;

    @Test
    public void testBuildPlayer() {
        Player player1 = Player.builder().name("xx").playerStatus(PlayerStatus.CONNECTED).id(5L).build();
        assertNotNull(player1);
    }

    @Test
    public void testSetName() {
        player.setName("a");
        assertEquals("a", player.getName());
    }

    @Test
    public void testSetPlayerStatus() {
        player.setPlayerStatus(PlayerStatus.CONNECTED);
        assertEquals(PlayerStatus.CONNECTED, player.getPlayerStatus());
    }

    @Test
    public void testGetId() {
        player.setId(1L);
        assertEquals(1L, player.getId());
    }
}
