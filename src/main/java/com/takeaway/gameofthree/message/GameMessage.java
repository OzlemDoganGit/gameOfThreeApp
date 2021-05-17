package com.takeaway.gameofthree.message;

import java.time.LocalDateTime;

import com.takeaway.gameofthree.enums.GameStatus;
import com.takeaway.gameofthree.enums.PlayType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameMessage {

    private String from;
    private Long fromId;
    private String to;
    private Long toId;
    private Long gameId;
    private GameStatus gameStatus;
    private LocalDateTime creationTime;
    private LocalDateTime updateTime;
    private PlayType playType;
    private Long playerId;
    private Integer number;
    private Integer move;

}
