package com.takeaway.gameofthree.service;

import java.util.List;

import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.message.GameMessage;

public interface PrepareAutoMessageService {

    public List<GameMessage> prepareAutoMessages(Game game, GameMessage gameMessage);

}
