package com.takeaway.gameofthree.service;

import java.util.List;

import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.domain.entity.Point;
import com.takeaway.gameofthree.dto.NickNameDTO;
import com.takeaway.gameofthree.message.GameMessage;

public interface GameOfThreeCommandService {

    public Game createOrJoinToTheGame(List<Game> gameList, NickNameDTO playerNickName);

    public Game updateTheGameStatus(Game game, GameMessage gameMessage);

    public Game updateTheGame(Game gameVal, GameMessage gameMessage);

    public Game updatePointOfAutoGame(Point point, Game game);

    public Game updateStatusOfAutoGame(Game game, GameMessage gameMessage);

}
