package com.takeaway.gameofthree.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.domain.entity.Player;
import com.takeaway.gameofthree.domain.entity.Point;
import com.takeaway.gameofthree.enums.GameStatus;
import com.takeaway.gameofthree.message.GameMessage;
import com.takeaway.gameofthree.service.GameOfThreeCommandService;
import com.takeaway.gameofthree.service.GameOfThreeQueryService;

import io.swagger.annotations.ApiOperation;

@Controller
public class GameOfThreeController {

	private static final String TOPICPREFIX = "/topic/gameOfThree/";
	private static final Integer END = Integer.valueOf(1);

	@Autowired
	private GameOfThreeCommandService gameOfThreeCommandService;

	@Autowired
	private GameOfThreeQueryService gameOfThreeQueryService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@GetMapping(value = "/joinToTheGame", consumes = { MediaType.ALL_VALUE })
	@ApiOperation(value = "", notes = "If there is no player create the game else join to the game.", nickname = "joinToTheGame")
	public ResponseEntity<Game> joinToTheGame() {
		Game game = setTheGame();
		return new ResponseEntity<>(game, HttpStatus.OK);
	}

	private Game setTheGame() {
		List<Game> waitingGameList = gameOfThreeQueryService.retriveWaitingGames();
		Game game = new Game();
		if (waitingGameList.isEmpty()) {
			// Mark the state as NOT_STARTED
			game = gameOfThreeCommandService.createTheGame();
		} else {
			// Mark the state as PAIRED
			game = gameOfThreeCommandService.addSecondPlayerToTheGame(waitingGameList.get(0));
		}
		return game;
	}

	@MessageMapping("/game.manualPlayGame")
	public void playGame(@Payload GameMessage gameMessage) {

		Optional<Game> game = gameOfThreeQueryService.retrieveTheGameById(gameMessage.getGameId());
		game.ifPresent(gameVal -> {
			Game updatedGame = gameOfThreeCommandService.updateTheGameStatus(gameVal, gameMessage);
			sendGameMessage(updatedGame, gameMessage);
		});
	}

	@MessageMapping("/game.calculateNumber")
	public void calculateNumber(@Payload GameMessage gameMessage) {

		Optional<Game> game = gameOfThreeQueryService.retrieveTheGameById(gameMessage.getGameId());
		game.ifPresent(gameVal -> {
			Game updatedGame = gameOfThreeCommandService.updateTheGame(gameVal, gameMessage);
			sendGameMessage(updatedGame, gameMessage);
		});
	}

	@MessageMapping("/game.autoPlayGame")
	public void autoPlayGame(@Payload GameMessage gameMessage) {

		Optional<Game> game = gameOfThreeQueryService.retrieveTheGameById(gameMessage.getGameId());

		game.ifPresent(gameVal -> {
			List<Game> autoGameResultList = new ArrayList<Game>();
			Integer placeOfPlayer = findThePlaceOfStartingPlayer(gameVal, gameMessage);
			Integer nextPlayerPlace = findThePlaceOfSecondPlayer(placeOfPlayer);
			Game statusUpdatedGame = gameOfThreeCommandService.updateStatusOfAutoGame(gameVal, placeOfPlayer);
			autoGameResultList.add(statusUpdatedGame);

			if (gameVal.getGameStatus() != GameStatus.WAITING_FOR_PLAYER) {
				Point point = gameVal.getPoint();
				while (point.getUpdatedNumber() != END) {
					Game updatedGame = gameOfThreeCommandService.updateGamePoint(point, gameVal);
					point = updatedGame.getPoint();
					autoGameResultList.add(updatedGame);
				}
				for (Game autoGameResult : autoGameResultList) {
					GameMessage sentGameMessage = autoGameResult.getGameStatus().buildAutoGameMessage(autoGameResult,
							placeOfPlayer, nextPlayerPlace);
					messagingTemplate.convertAndSend(TOPICPREFIX.concat(String.valueOf(autoGameResult.getId())),
							sentGameMessage);
					Integer startingPlayerIndexTemp = nextPlayerPlace;
					nextPlayerPlace = updateNextPlayerPlace(placeOfPlayer, nextPlayerPlace);
					placeOfPlayer = updateStartingPlayerPlace(placeOfPlayer, startingPlayerIndexTemp);
				}
			} else {
				GameMessage sentGameMessage = gameVal.getGameStatus().buildAutoGameMessage(gameVal, placeOfPlayer,
						nextPlayerPlace);
				messagingTemplate.convertAndSend(TOPICPREFIX.concat(String.valueOf(gameVal.getId())), sentGameMessage);

			}
		});
	}

	private Integer updateStartingPlayerPlace(Integer placeOfPlayer, Integer startingPlayerIndexTemp) {
		placeOfPlayer = startingPlayerIndexTemp;
		return placeOfPlayer;
	}

	private Integer updateNextPlayerPlace(Integer placeOfPlayer, Integer nextPlayerPlace) {
		nextPlayerPlace = placeOfPlayer;
		return nextPlayerPlace;
	}

	private void sendGameMessage(Game game, GameMessage gameMessage) {
		gameMessage = game.getGameStatus().buildManualGameMessage(game, gameMessage);
		messagingTemplate.convertAndSend(TOPICPREFIX.concat(String.valueOf(game.getId())),gameMessage);
	}

	private Integer findThePlaceOfStartingPlayer(Game game, GameMessage gameMessage) {
		List<Player> playerList = game.getPlayerList();
		Integer placeOfIndex = Integer.valueOf(0);
		for (Player player : playerList) {
			if (gameMessage.getPlayerId().equals(player.getId())) {
				placeOfIndex = playerList.indexOf(player);
			}
		}
		return placeOfIndex;
	}

	private Integer findThePlaceOfSecondPlayer(Integer placeOfPlayer) {

		Integer nextPlayerIndex = Integer.valueOf(0);
		if (placeOfPlayer.equals(Integer.valueOf(0))) {
			nextPlayerIndex = Integer.valueOf(1);
		} else {
			nextPlayerIndex = Integer.valueOf(0);
		}
		return nextPlayerIndex;
	}

}
