package com.takeaway.gameofthree.service;

import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.domain.entity.Point;
import com.takeaway.gameofthree.message.GameMessage;

public interface GameOfThreeCommandService {

	public Game createTheGame();

	public Game addSecondPlayerToTheGame(Game game);

	public Game updateTheGameStatus(Game game, GameMessage gameMessage);

	public Game updateTheGame(Game gameVal, GameMessage gameMessage);

	public Game updateGamePoint(Point point, Game game);

	public Game updateStatusOfAutoGame(Game game, Integer placeOfPlayer);
	
	
}
