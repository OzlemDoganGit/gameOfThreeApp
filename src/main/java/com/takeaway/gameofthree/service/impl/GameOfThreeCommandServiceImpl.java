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
import com.takeaway.gameofthree.service.FindPlayerPlaceService;
import com.takeaway.gameofthree.service.GameOfThreeCommandService;
import com.takeaway.gameofthree.service.NumberGeneratorService;
import com.takeaway.gameofthree.utils.ValidatedNumber;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
public class GameOfThreeCommandServiceImpl implements GameOfThreeCommandService {

    private static final Integer ONLY_ONE_PLAYER = Integer.valueOf(1);
    private static final String PLAYER = "PLAYER-";
    private static final Integer END = Integer.valueOf(1);
    private static final Integer ARITHMETICCONSTANT = Integer.valueOf(3);
    private static final Integer ADDONE = Integer.valueOf(1);
    private static final Integer MINUSONE = Integer.valueOf(-1);
    private static final Integer ZERO = Integer.valueOf(0);
    private static final Integer ONE = Integer.valueOf(1);

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private FindPlayerPlaceService findPlayerService;

    @Autowired
    @Qualifier("randomNumberGeneratorService")
    private NumberGeneratorService numberGeneratorService;

    public Game createTheGame() {
        Player player = updatePlayer();
        List<Player> playerList = new ArrayList<>();
        playerList.add(player);
        LocalDateTime now = LocalDateTime.now();
        Point point = generateRandomPoint();
        Game game = Game.builder().gameStatus(GameStatus.NOT_STARTED).playerList(playerList).creationTime(now).point(point).build();
        return gameRepository.save(game);
    }

    private Player updatePlayer() {
        String name = PLAYER.concat(String.valueOf(numberGeneratorService.generateRandomNumber()));
        return Player.builder().name(name).playerStatus(PlayerStatus.CONNECTED).build();
    }
    public Game addSecondPlayerToTheGame(Game game) {
        Player player = updatePlayer();
        game.addPlayerList(player);
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
            game.getPlayerList().stream().forEach(e -> e.setPlayerStatus(PlayerStatus.CONNECTED));
            game.setPlayType(PlayType.MANUAL_PLAY);
            game.setUpdateTime(LocalDateTime.now());
        }
        return gameRepository.save(game);
    }

    private Point generateRandomPoint() {
        Integer generatedNumber = generateRandomNumber();
        return Point.builder().startedNumber(generatedNumber).updatedNumber(generatedNumber).build();
    }

    private Integer generateRandomNumber() {
        return numberGeneratorService.generateRandomNumber();
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
        return (validatedNumber.getMove() + validatedNumber.getNumber()) / ARITHMETICCONSTANT;
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
        game.setUpdateTime(LocalDateTime.now());
        game.setGameStatus(updateTheGameStatus(updatedNumber));
        Point point = game.getPoint();
        point.setUpdatedNumber(updatedNumber);
        point.setAdjustedNumber(validatedNumber.getMove());
        point.setOperator(ArithmeticOperator.DIVIDE);
        point.setArithmeticNumber(ARITHMETICCONSTANT);
        game.setPoint(point);
        return gameRepository.save(game);
    }

    private GameStatus updateTheGameStatus(Integer updatedNumber) {
        if (updatedNumber.equals(END)) {
            return GameStatus.FINALIZED;
        } else {
            return GameStatus.ON_GOING;
        }
    }

    @Override
    public Game updatePointOfAutoGame(Point point, Game game) {
        Point pointTemp = preparePoint(point, game);
        game.setPoint(pointTemp);
        game.setGameStatus(updateTheGameStatus(pointTemp.getUpdatedNumber()));
        game.setUpdateTime(LocalDateTime.now());
        return gameRepository.save(game);

    }

    private Point preparePoint(Point point, Game game) {

        Integer adjustment = findAdjustment(game.getPoint().getUpdatedNumber());
        Integer updatedNumber = (point.getUpdatedNumber() + adjustment) / ARITHMETICCONSTANT;
        return Point.builder().adjustedNumber(adjustment).updatedNumber(updatedNumber)
                .arithmeticNumber(ARITHMETICCONSTANT).operator(ArithmeticOperator.DIVIDE)
                .startedNumber(game.getPoint().getStartedNumber()).build();
    }

    private Integer findAdjustment(Integer updatedNumber) {
        if (((updatedNumber + ONE) % ARITHMETICCONSTANT) == ZERO) {
            return ADDONE;
        } else if (((updatedNumber - ONE) % ARITHMETICCONSTANT) == ZERO) {
            return MINUSONE;
        } else {
            return ZERO;
        }
    }

    @Override
    public Game updateStatusOfAutoGame(Game game, GameMessage gameMessage) {
        Integer placeOfPlayer = findPlayerService.findThePlaceOfStartingPlayer(game, gameMessage);
        if (game.getPlayerList().size() == ONLY_ONE_PLAYER) {
            game.setGameStatus(GameStatus.WAITING_FOR_PLAYER);
            game.getPlayerList().get(placeOfPlayer).setPlayerStatus(PlayerStatus.CONNECTED);
            game.setUpdateTime(LocalDateTime.now());
        } else {
            game.setUpdateTime(LocalDateTime.now());
            game.getPlayerList().stream().forEach(e -> e.setPlayerStatus(PlayerStatus.CONNECTED));
            game.setGameStatus(GameStatus.STARTED);
            game.setPlayType(PlayType.AUTOMATIC_PLAY);
        }
        return gameRepository.save(game);
    }

    @Override
    public Game createOrJoinToTheGame(List<Game> waitingGameList) {
        if (waitingGameList.isEmpty()) {
            // Mark the state as NOT_STARTED
            return createTheGame();
        } else {
            // Mark the state as PAIRED
            return addSecondPlayerToTheGame(waitingGameList.get(0));
        }
    }

}
