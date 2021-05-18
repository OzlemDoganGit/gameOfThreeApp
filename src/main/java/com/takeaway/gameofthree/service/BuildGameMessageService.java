package com.takeaway.gameofthree.service;

import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.message.GameMessage;

public interface BuildGameMessageService {

    public abstract GameMessage buildAutoGameMessage(Game autoGameResult, Integer placeOfPlayer,
            Integer nextPlayerPlace);

    public abstract GameMessage buildManualGameMessage(Game game, GameMessage gameMessage);
}
