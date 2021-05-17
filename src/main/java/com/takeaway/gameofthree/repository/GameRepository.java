package com.takeaway.gameofthree.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.enums.GameStatus;

public interface GameRepository extends JpaRepository<Game, Long> {

	public List<Game> findByGameStatus(GameStatus gameStatus);
}
