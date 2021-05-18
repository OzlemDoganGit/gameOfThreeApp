package com.takeaway.gameofthree.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.domain.entity.Player;
import com.takeaway.gameofthree.enums.GameStatus;
import com.takeaway.gameofthree.message.GameMessage;
import com.takeaway.gameofthree.service.BuildGameMessageService;

@Component
public class BuildGameMessageServiceImpl implements BuildGameMessageService {

    @Override
    public GameMessage buildAutoGameMessage(Game autoGameResult, Integer placeOfPlayer, Integer nextPlayerPlace) {
        if (autoGameResult.getGameStatus().equals(GameStatus.STARTED)) { // no move
            return GameMessage.builder().from(autoGameResult.getPlayerList().get(placeOfPlayer).getName())
                    .gameId(autoGameResult.getId()).gameStatus(autoGameResult.getGameStatus())
                    .to(autoGameResult.getPlayerList().get(nextPlayerPlace).getName())
                    .number(autoGameResult.getPoint().getUpdatedNumber()).build();
        } else if (autoGameResult.getGameStatus().equals(GameStatus.WAITING_FOR_PLAYER)) { // no to
            return GameMessage.builder().from(autoGameResult.getPlayerList().get(placeOfPlayer).getName())
                    .gameId(autoGameResult.getId()).gameStatus(autoGameResult.getGameStatus())
                    .number(autoGameResult.getPoint().getUpdatedNumber()).build();
        }
        return GameMessage.builder().from(autoGameResult.getPlayerList().get(placeOfPlayer).getName())
                .gameId(autoGameResult.getId()).gameStatus(autoGameResult.getGameStatus())
                .to(autoGameResult.getPlayerList().get(nextPlayerPlace).getName())
                .move(autoGameResult.getPoint().getAdjustedNumber())
                .number(autoGameResult.getPoint().getUpdatedNumber()).build();

    }

    @Override
    public GameMessage buildManualGameMessage(Game game, GameMessage gameMessage) {
        Player senderPlayer = findTheSenderPlayerName(game, gameMessage);
        Player receiverPlayer = findReceiverPlayerName(game, gameMessage);
        if (game.getGameStatus().equals(GameStatus.WAITING_FOR_PLAYER)) {

            return GameMessage.builder().gameId(game.getId()).number(game.getPoint().getUpdatedNumber())
                    .gameStatus(game.getGameStatus()).playType(game.getPlayType()).build();
        } else if (game.getGameStatus().equals(GameStatus.STARTED)) {
            return GameMessage.builder().gameId(game.getId()).number(game.getPoint().getUpdatedNumber())
                    .gameStatus(game.getGameStatus()).to(receiverPlayer.getName()).toId(receiverPlayer.getId())
                    .playType(game.getPlayType()).build();
        }
        return GameMessage.builder().gameId(game.getId()).number(game.getPoint().getUpdatedNumber())
                .from(senderPlayer.getName()).gameStatus(game.getGameStatus()).to(receiverPlayer.getName())
                .fromId(senderPlayer.getId()).toId(receiverPlayer.getId()).move(gameMessage.getMove())
                .playType(game.getPlayType()).build();

    }

    private Player findTheSenderPlayerName(Game game, GameMessage gameMessage) {
        List<Player> playerList = game.getPlayerList();
        Player senderPlayer = new Player();
        for (Player player : playerList) {
            if (gameMessage.getPlayerId().equals(player.getId())) {
                senderPlayer = player;
            }
        }
        return senderPlayer;
    }

    public Player findReceiverPlayerName(Game game, GameMessage gameMessage) {
        List<Player> playerList = game.getPlayerList();
        Player receivedPlayer = new Player();
        for (Player player : playerList) {
            if (!gameMessage.getPlayerId().equals(player.getId())) {
                receivedPlayer = player;
            }
        }
        return receivedPlayer;
    }

}
