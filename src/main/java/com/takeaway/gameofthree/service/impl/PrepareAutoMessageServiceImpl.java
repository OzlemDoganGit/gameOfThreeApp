package com.takeaway.gameofthree.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.domain.entity.Point;
import com.takeaway.gameofthree.enums.GameStatus;
import com.takeaway.gameofthree.message.GameMessage;
import com.takeaway.gameofthree.service.BuildGameMessageService;
import com.takeaway.gameofthree.service.FindPlayerPlaceService;
import com.takeaway.gameofthree.service.GameOfThreeCommandService;
import com.takeaway.gameofthree.service.PrepareAutoMessageService;

@Component
public class PrepareAutoMessageServiceImpl implements PrepareAutoMessageService {

    @Autowired
    FindPlayerPlaceService findPlayerService;

    @Autowired
    GameOfThreeCommandService gameOfThreeCommandService;

    @Autowired
    private BuildGameMessageService buildGameMessageService;

    private static final Integer END = Integer.valueOf(1);

    @Override
    public List<GameMessage> prepareAutoMessages(Game game, GameMessage gameMessage) {

        List<Game> autoGameResultList = new ArrayList<>();
        List<GameMessage> autoGameMessageList = new ArrayList<>();

        Game statusUpdatedGame = gameOfThreeCommandService.updateStatusOfAutoGame(game, gameMessage);
        autoGameResultList.add(statusUpdatedGame);
        Integer placeOfPlayer = findPlayerService.findThePlaceOfStartingPlayer(game, gameMessage);
        Integer nextPlayerPlace = findPlayerService.findThePlaceOfSecondPlayer(placeOfPlayer);
        if (!statusUpdatedGame.getGameStatus().equals(GameStatus.WAITING_FOR_PLAYER)) {
            Point point = game.getPoint();
            while (!point.getUpdatedNumber().equals(END)) {
                Game updatedGame = gameOfThreeCommandService.updatePointOfAutoGame(point, game);
                point = updatedGame.getPoint();
                autoGameResultList.add(updatedGame);
            }
        }

        for (Game autoGameResult : autoGameResultList) {
            GameMessage sentGameMessage = buildGameMessageService.buildAutoGameMessage(autoGameResult, placeOfPlayer,
                    nextPlayerPlace);
            autoGameMessageList.add(sentGameMessage);
            // Replace the place in each message
            Integer startingPlayerIndexTemp = nextPlayerPlace;
            nextPlayerPlace = placeOfPlayer;
            placeOfPlayer = startingPlayerIndexTemp;
        }

        return autoGameMessageList;

    }

}
