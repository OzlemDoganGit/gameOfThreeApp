package com.takeaway.gameofthree.enums;

import java.util.List;

import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.domain.entity.Player;
import com.takeaway.gameofthree.message.GameMessage;

public enum GameStatus {

	NOT_STARTED() {

		@Override
		public GameMessage buildAutoGameMessage(Game autoGameResult, Integer placeOfPlayer, Integer nextPlayerPlace) {
			return null;
		}

		@Override
		public GameMessage buildManualGameMessage(Game game, GameMessage gameMessage) {
			Player player = findReceiverPlayerName(game, gameMessage);
			GameMessage.builder().gameId(game.getId()).number(game.getPoint().getStartedNumber())
					.gameStatus(game.getGameStatus()).to(player.getName()).creationTime(game.getCreationTime())
					.toId(player.getId()).updateTime(game.getUpdateTime()).playType(game.getPlayType()).build();
			return null;
		}

		@Override
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
	},
	WAITING_FOR_PLAYER() {

		@Override
		public GameMessage buildAutoGameMessage(Game autoGameResult, Integer placeOfPlayer, Integer nextPlayerPlace) {
			return GameMessage.builder().gameId(autoGameResult.getId()).gameStatus(autoGameResult.getGameStatus())
					.creationTime(autoGameResult.getCreationTime()).updateTime(autoGameResult.getUpdateTime())
					.playType(autoGameResult.getPlayType()).build();
		}

		@Override
		public GameMessage buildManualGameMessage(Game game, GameMessage gameMessage) {
			return GameMessage.builder().gameId(game.getId()).gameStatus(game.getGameStatus())
					.creationTime(game.getCreationTime()).updateTime(game.getUpdateTime()).playType(game.getPlayType())
					.build();
		}

		@Override
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
	},

	PAIRED() {

		@Override
		public GameMessage buildAutoGameMessage(Game autoGameResult, Integer placeOfPlayer, Integer nextPlayerPlace) {
			return null;
		}

		@Override
		public GameMessage buildManualGameMessage(Game game, GameMessage gameMessage) {
			Player player = findReceiverPlayerName(game, gameMessage);
			return GameMessage.builder().gameId(game.getId()).number(game.getPoint().getStartedNumber())
					.gameStatus(game.getGameStatus()).to(player.getName()).creationTime(game.getCreationTime())
					.toId(player.getId()).updateTime(game.getUpdateTime()).playType(game.getPlayType()).build();
		}

		@Override
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
	},
	READY() {

		@Override
		public GameMessage buildAutoGameMessage(Game autoGameResult, Integer placeOfPlayer, Integer nextPlayerPlace) {
			return GameMessage.builder().from(autoGameResult.getPlayerList().get(placeOfPlayer).getName())
					.gameId(autoGameResult.getId()).gameStatus(autoGameResult.getGameStatus())
					.to(autoGameResult.getPlayerList().get(nextPlayerPlace).getName())
					.move(autoGameResult.getPoint().getAdjustedNumber())
					.number(autoGameResult.getPoint().getUpdatedNumber()).build();
		}

		@Override
		public GameMessage buildManualGameMessage(Game game, GameMessage gameMessage) {
			Player player = findReceiverPlayerName(game, gameMessage);
			return GameMessage.builder().gameId(game.getId()).number(game.getPoint().getStartedNumber())
					.gameStatus(game.getGameStatus()).to(player.getName()).creationTime(game.getCreationTime())
					.toId(player.getId()).updateTime(game.getUpdateTime()).playType(game.getPlayType()).build();
		}

		@Override
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
	},
	STARTED() {

		@Override
		public GameMessage buildAutoGameMessage(Game autoGameResult, Integer placeOfPlayer, Integer nextPlayerPlace) {
			return GameMessage.builder().from(autoGameResult.getPlayerList().get(placeOfPlayer).getName())
					.gameId(autoGameResult.getId()).gameStatus(autoGameResult.getGameStatus())
					.to(autoGameResult.getPlayerList().get(nextPlayerPlace).getName())
					.move(autoGameResult.getPoint().getAdjustedNumber())
					.number(autoGameResult.getPoint().getUpdatedNumber()).build();

		}

		@Override
		public GameMessage buildManualGameMessage(Game game, GameMessage gameMessage) {
			Player player = findReceiverPlayerName(game, gameMessage);
			return GameMessage.builder().gameId(game.getId()).number(game.getPoint().getStartedNumber())
					.gameStatus(game.getGameStatus()).to(player.getName()).creationTime(game.getCreationTime())
					.toId(player.getId()).updateTime(game.getUpdateTime()).playType(game.getPlayType()).build();
		}

		@Override
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
	},
	ON_GOING() {

		@Override
		public GameMessage buildAutoGameMessage(Game autoGameResult, Integer placeOfPlayer, Integer nextPlayerPlace) {
			return GameMessage.builder().from(autoGameResult.getPlayerList().get(placeOfPlayer).getName())
					.gameId(autoGameResult.getId()).gameStatus(autoGameResult.getGameStatus())
					.to(autoGameResult.getPlayerList().get(nextPlayerPlace).getName())
					.move(autoGameResult.getPoint().getAdjustedNumber())
					.number(autoGameResult.getPoint().getUpdatedNumber()).build();

		}

		@Override
		public GameMessage buildManualGameMessage(Game game, GameMessage gameMessage) {
			Player player = findReceiverPlayerName(game, gameMessage);

			return GameMessage.builder().gameId(game.getId()).number(game.getPoint().getUpdatedNumber())
					.gameStatus(game.getGameStatus()).to(player.getName()).creationTime(game.getCreationTime())
					.toId(player.getId()).move(game.getPoint().getAdjustedNumber()).updateTime(game.getUpdateTime())
					.playType(game.getPlayType()).build();

		}

		@Override
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
	},
	FINALIZED() {

		@Override
		public GameMessage buildAutoGameMessage(Game autoGameResult, Integer placeOfPlayer, Integer nextPlayerPlace) {
			return GameMessage.builder().from(autoGameResult.getPlayerList().get(placeOfPlayer).getName())
					.gameId(autoGameResult.getId()).gameStatus(autoGameResult.getGameStatus())
					.to(autoGameResult.getPlayerList().get(nextPlayerPlace).getName())
					.move(autoGameResult.getPoint().getAdjustedNumber())
					.number(autoGameResult.getPoint().getUpdatedNumber()).build();

		}

		@Override
		public GameMessage buildManualGameMessage(Game game, GameMessage gameMessage) {
			Player player = findReceiverPlayerName(game, gameMessage);

			return GameMessage.builder().gameId(game.getId()).number(game.getPoint().getUpdatedNumber())
					.gameStatus(game.getGameStatus()).to(player.getName()).creationTime(game.getCreationTime())
					.toId(player.getId()).move(game.getPoint().getAdjustedNumber()).updateTime(game.getUpdateTime())
					.playType(game.getPlayType()).build();

		}

		@Override
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
	},
	ERROR() {

		@Override
		public GameMessage buildAutoGameMessage(Game autoGameResult, Integer placeOfPlayer, Integer nextPlayerPlace) {
			return null;
		}

		@Override
		public GameMessage buildManualGameMessage(Game game, GameMessage gameMessage) {
			Player senderPlayer = findTheSenderPlayerName(game, gameMessage);
			Player receiverPlayer = findReceiverPlayerName(game, gameMessage);
			return GameMessage.builder().gameId(game.getId()).number(gameMessage.getNumber())
					.from(senderPlayer.getName()).gameStatus(game.getGameStatus()).to(receiverPlayer.getName())
					.fromId(senderPlayer.getId()).creationTime(game.getCreationTime()).toId(receiverPlayer.getId())
					.move(gameMessage.getMove()).updateTime(game.getUpdateTime()).playType(game.getPlayType()).build();

		}

		@Override
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
	},
	DISCONNECTED() {

		@Override
		public GameMessage buildAutoGameMessage(Game autoGameResult, Integer placeOfPlayer, Integer nextPlayerPlace) {
			return GameMessage.builder().from(autoGameResult.getPlayerList().get(placeOfPlayer).getName())
					.gameId(autoGameResult.getId()).gameStatus(autoGameResult.getGameStatus())
					.to(autoGameResult.getPlayerList().get(nextPlayerPlace).getName())
					.move(autoGameResult.getPoint().getAdjustedNumber())
					.number(autoGameResult.getPoint().getUpdatedNumber()).build();

		}

		@Override
		public GameMessage buildManualGameMessage(Game game, GameMessage gameMessage) {
			Player player = findReceiverPlayerName(game, gameMessage);
			return GameMessage.builder().gameId(game.getId()).number(game.getPoint().getStartedNumber())
					.gameStatus(game.getGameStatus()).to(player.getName()).creationTime(game.getCreationTime())
					.toId(player.getId()).updateTime(game.getUpdateTime()).playType(game.getPlayType()).build();
		}

		@Override
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
	};

	public abstract GameMessage buildAutoGameMessage(Game autoGameResult, Integer placeOfPlayer,
			Integer nextPlayerPlace);

	public abstract GameMessage buildManualGameMessage(Game game, GameMessage gameMessage);

	public abstract Player findReceiverPlayerName(Game game, GameMessage gameMessage);

}
