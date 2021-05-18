package com.takeaway.gameofthree.service;

import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.message.GameMessage;

public interface FindPlayerPlaceService {

    public Integer findThePlaceOfStartingPlayer(Game game, GameMessage gameMessage);

    public Integer findThePlaceOfSecondPlayer(Integer placeOfPlayer);
}
