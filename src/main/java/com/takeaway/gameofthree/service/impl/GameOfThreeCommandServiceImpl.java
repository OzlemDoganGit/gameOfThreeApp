package com.takeaway.gameofthree.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.domain.entity.Player;
import com.takeaway.gameofthree.domain.entity.Point;
import com.takeaway.gameofthree.enums.ArithmeticOperator;
import com.takeaway.gameofthree.enums.GameStatus;
import com.takeaway.gameofthree.enums.PlayType;
import com.takeaway.gameofthree.enums.PlayerStatus;
import com.takeaway.gameofthree.enums.ValidationStatus;
import com.takeaway.gameofthree.message.GameMessage;
import com.takeaway.gameofthree.repository.GameRepository;
import com.takeaway.gameofthree.service.GameOfThreeCommandService;
import com.takeaway.gameofthree.service.NumberGeneratorService;
import com.takeaway.gameofthree.utils.ValidatedNumber;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
public class GameOfThreeCommandServiceImpl implements GameOfThreeCommandService {

	private static final Integer ONLY_ONE_PLAYER = Integer.valueOf(1);
	private static final String PLAYER = "PLAYER-";
	private static final Integer END = Integer.valueOf(1);

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	@Qualifier("randomNumberGeneratorService")
	private NumberGeneratorService numberGeneratorService;

	@Override
	public Game createTheGame() {
		Player player = updatePlayer();
		List<Player> playerList = new ArrayList<Player>();
		playerList.add(player);
		LocalDateTime now = LocalDateTime.now();
		Game game = Game.builder().gameStatus(GameStatus.NOT_STARTED).playerList(playerList).creationTime(now).build();
		return gameRepository.save(game);
	}

	private Player updatePlayer() {
		String name = PLAYER.concat(String.valueOf(numberGeneratorService.generateRandomNumber()));
		Player player = Player.builder().name(name).playerStatus(PlayerStatus.CONNECTED).build();
		return player;
	}

	@Override
	public Game addSecondPlayerToTheGame(Game game) {
		Player player = updatePlayer();
		game.addPlayerList(player);
		Point point = generateRandomPoint();
		game.setPoint(point);
		game.setGameStatus(GameStatus.READY);
		game.setUpdateTime(LocalDateTime.now());
		game.setGameStatus(GameStatus.PAIRED);
		return gameRepository.save(game);
	}

	@Override
	public Game updateTheGameStatus(Game game, GameMessage gameMessage) {

		if (game.getPlayerList().size() == ONLY_ONE_PLAYER) {
			game.setGameStatus(GameStatus.WAITING_FOR_PLAYER);
			game.getPlayerList().stream().filter(e -> gameMessage.getPlayerId().equals(e.getId())).findAny()
					.ifPresent(player -> player.setPlayerStatus(PlayerStatus.CONNECTED));
			game.setUpdateTime(LocalDateTime.now());
		} else {
			game.setGameStatus(GameStatus.STARTED);
			game.getPlayerList().stream().forEach(e -> {
				e.setPlayerStatus(PlayerStatus.CONNECTED);
			});
			game.setPlayType(PlayType.MANUAL_PLAY);
			game.setUpdateTime(LocalDateTime.now());
		}
		return gameRepository.save(game);
	}

	private Point generateRandomPoint() {
		Integer generatedNumber = generateRandomNumber();
		Point point = Point.builder().startedNumber(generatedNumber).updatedNumber(generatedNumber).build();
		return point;
	}

	private Integer generateRandomNumber() {
		Integer generatedNumber = numberGeneratorService.generateRandomNumber();
		return generatedNumber;
	}

	@Override
	public Game updateTheGame(Game game, GameMessage gameMessage) {
		ValidatedNumber validatedNumber = processTheNumber(gameMessage);
		if (validatedNumber.getStatus() == ValidationStatus.VALID) {
			return updateTheGamePointAndStatus(validatedNumber, game);
		} else {
			game.setUpdateTime(LocalDateTime.now());
			game.setGameStatus(GameStatus.ERROR);
			return gameRepository.save(game);
		}
	}

	private Integer findTheUpdatedNumber(ValidatedNumber validatedNumber) {
		Integer arithmeticValue = Integer.valueOf(3);
		Integer updatedNumber = (validatedNumber.getMove() + validatedNumber.getNumber()) / arithmeticValue;
		return updatedNumber;

	}

	public ValidatedNumber processTheNumber(GameMessage gameMessage) {
		ValidationStatus status = validateTheReceivedNumber(gameMessage.getNumber(), gameMessage.getMove());
		return ValidatedNumber.builder().number(gameMessage.getNumber()).move(gameMessage.getMove()).status(status)
				.build();
	}

	ValidationStatus validateTheReceivedNumber(Integer number, Integer move) {
		if ((number + move) % 3 == 0) {
			return ValidationStatus.VALID;
		} else {
			return ValidationStatus.NOT_VALID;
		}

	}

	private Game updateTheGamePointAndStatus(ValidatedNumber validatedNumber, Game game) {
		Integer updatedNumber = findTheUpdatedNumber(validatedNumber);
		Integer arithmeticValue = Integer.valueOf(3);
		game.setUpdateTime(LocalDateTime.now());
		game.setGameStatus(updateTheGameStatus(updatedNumber));
		Point point = game.getPoint();
		point.setUpdatedNumber(updatedNumber);
		point.setAdjustedNumber(validatedNumber.getMove());
		point.setOperator(ArithmeticOperator.DIVIDE);
		point.setArithmeticNumber(arithmeticValue);
		game.setPoint(point);
		return gameRepository.save(game);
	}

	private GameStatus updateTheGameStatus(Integer updatedNumber) {
		if (updatedNumber == END) {
			return GameStatus.FINALIZED;
		} else {
			return GameStatus.ON_GOING;
		}
	}

	@Override
	public Game updateGamePoint(Point point, Game game) {
		Integer arithmeticValue = Integer.valueOf(3);
		Point pointTemp = preparePoint(point, arithmeticValue, game);
		game.setPoint(pointTemp);
		game.setGameStatus(updateTheGameStatus(pointTemp.getUpdatedNumber()));
		game.setUpdateTime(LocalDateTime.now());
		return gameRepository.save(game);

	}

	private Point preparePoint(Point point, Integer arithmeticValue, Game game) {
		Integer adjustment = Integer.valueOf(0);
		Integer updatedNumber = Integer.valueOf(0);
		adjustment = findAdjustment(game.getPoint().getUpdatedNumber(), arithmeticValue);
		updatedNumber = (point.getUpdatedNumber() + adjustment) / arithmeticValue;
		return Point.builder().adjustedNumber(adjustment).updatedNumber(updatedNumber).arithmeticNumber(arithmeticValue)
				.operator(ArithmeticOperator.DIVIDE).startedNumber(game.getPoint().getStartedNumber()).build();
	}

	private Integer findAdjustment(Integer updatedNumber, Integer arithmeticValue) {
		if (((updatedNumber + 1) % 3) == 0) {
			return Integer.valueOf(1);
		} else if (((updatedNumber - 1) % 3) == 0) {
			return Integer.valueOf(-1);
		} else {
			return Integer.valueOf(0);
		}
	}

	@Override
	public Game updateStatusOfAutoGame(Game game, Integer placeOfPlayer) {
		if (game.getPlayerList().size() == ONLY_ONE_PLAYER) {
			game.setGameStatus(GameStatus.WAITING_FOR_PLAYER);
			game.getPlayerList().get(placeOfPlayer).setPlayerStatus(PlayerStatus.CONNECTED);
			game.setUpdateTime(LocalDateTime.now());
		} else {
			game.setUpdateTime(LocalDateTime.now());
			game.setGameStatus(GameStatus.STARTED);
		}
		return gameRepository.save(game);
	}

}
