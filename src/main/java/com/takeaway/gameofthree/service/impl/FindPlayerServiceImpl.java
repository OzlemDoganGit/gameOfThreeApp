package com.takeaway.gameofthree.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.domain.entity.Player;
import com.takeaway.gameofthree.message.GameMessage;
import com.takeaway.gameofthree.service.FindPlayerPlaceService;

@Component
public class FindPlayerServiceImpl implements FindPlayerPlaceService {

    public Integer findThePlaceOfStartingPlayer(Game game, GameMessage gameMessage) {
        List<Player> playerList = game.getPlayerList();
        Integer placeOfIndex = Integer.valueOf(0);
        for (Player player : playerList) {
            if (gameMessage.getPlayerId().equals(player.getId())) {
                placeOfIndex = playerList.indexOf(player);
            }
        }
        return placeOfIndex;
    }

    public Integer findThePlaceOfSecondPlayer(Integer placeOfPlayer) {

        if (placeOfPlayer.equals(Integer.valueOf(0))) {
            return Integer.valueOf(1);
        } else {
            return Integer.valueOf(0);
        }

    }
}
