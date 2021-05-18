package com.takeaway.gameofthree.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.takeaway.gameofthree.enums.GameStatus;
import com.takeaway.gameofthree.enums.PlayType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue
    private long id;

    @Column(columnDefinition = "TIMESTAMP", updatable = false, nullable = true)
    LocalDateTime creationTime;

    @Column(columnDefinition = "TIMESTAMP", updatable = true, nullable = true)
    LocalDateTime updateTime;

    @Column(nullable = true)
    GameStatus gameStatus;

    @Column(nullable = true)
    PlayType playType;

    @Column(nullable = true)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    List<Player> playerList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "point_id", referencedColumnName = "id")
    Point point;

    public void addPlayerList(Player player) {
        List<Player> existingPlayerList = this.getPlayerList();
        this.getPlayerList().add(player);
        this.setPlayerList(existingPlayerList);
    }

}
