package com.takeaway.gameofthree.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.dto.NickNameDTO;
import com.takeaway.gameofthree.message.GameMessage;
import com.takeaway.gameofthree.service.BuildGameMessageService;
import com.takeaway.gameofthree.service.FindPlayerPlaceService;
import com.takeaway.gameofthree.service.GameOfThreeCommandService;
import com.takeaway.gameofthree.service.GameOfThreeQueryService;
import com.takeaway.gameofthree.service.PrepareAutoMessageService;

import io.swagger.annotations.ApiOperation;

@Controller
public class GameOfThreeController {

    private static final String TOPICPREFIX = "/topic/gameOfThree/";

    @Autowired
    private GameOfThreeCommandService gameOfThreeCommandService;

    @Autowired
    private GameOfThreeQueryService gameOfThreeQueryService;

    @Autowired
    private FindPlayerPlaceService findPlayerService;

    @Autowired
    private BuildGameMessageService buildGameMessageService;

    @Autowired
    private PrepareAutoMessageService prepareAutoMessageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping(value = "/joinToTheGame", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value = "", notes = "If there is no player create the game else join to the game.",
            nickname = "joinToTheGame")
    public ResponseEntity<Game> joinToTheGame(@RequestBody NickNameDTO playerNickName) {
        List<Game> waitingGameList = gameOfThreeQueryService.retriveWaitingGames();
        Game game = gameOfThreeCommandService.createOrJoinToTheGame(waitingGameList, playerNickName);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @MessageMapping("/game.manualPlayGame")
    public void manualPlayGame(@Payload GameMessage gameMessage) {

        Optional<Game> game = gameOfThreeQueryService.retrieveTheGameById(gameMessage.getGameId());
        game.ifPresent(gameVal -> {
            Game updatedGame = gameOfThreeCommandService.updateTheGameStatus(gameVal, gameMessage);
            GameMessage updatedGameMessage = buildGameMessageService.buildManualGameMessage(updatedGame, gameMessage);
            messagingTemplate.convertAndSend(TOPICPREFIX.concat(String.valueOf(updatedGame.getId())),
                    updatedGameMessage);
        });
    }

    @MessageMapping("/game.calculateNumber")
    public void calculateNumber(@Payload GameMessage gameMessage) {

        Optional<Game> game = gameOfThreeQueryService.retrieveTheGameById(gameMessage.getGameId());
        game.ifPresent(gameVal -> {
            Game updatedGame = gameOfThreeCommandService.updateTheGame(gameVal, gameMessage);
            GameMessage updatedGameMessage = buildGameMessageService.buildManualGameMessage(updatedGame, gameMessage);
            messagingTemplate.convertAndSend(TOPICPREFIX.concat(String.valueOf(updatedGame.getId())),
                    updatedGameMessage);
        });
    }

    @MessageMapping("/game.autoPlayGame")
    public void autoPlayGame(@Payload GameMessage gameMessage) {

        Optional<Game> game = gameOfThreeQueryService.retrieveTheGameById(gameMessage.getGameId());
        game.ifPresent(gameVal -> {
            List<GameMessage> gameMessageList = prepareAutoMessageService.prepareAutoMessages(gameVal, gameMessage);
            for (GameMessage sentGameMessage : gameMessageList) {
                messagingTemplate.convertAndSend(TOPICPREFIX.concat(String.valueOf(gameMessage.getGameId())),
                        sentGameMessage);
            }
            if (gameMessageList.isEmpty()) {
                Integer placeOfPlayer = findPlayerService.findThePlaceOfStartingPlayer(gameVal, gameMessage);
                Integer nextPlayerPlace = findPlayerService.findThePlaceOfSecondPlayer(placeOfPlayer);
                GameMessage sentGameMessage = buildGameMessageService.buildAutoGameMessage(gameVal, placeOfPlayer,
                        nextPlayerPlace);
                messagingTemplate.convertAndSend(TOPICPREFIX.concat(String.valueOf(gameVal.getId())), sentGameMessage);
            }
        });
    }

}
