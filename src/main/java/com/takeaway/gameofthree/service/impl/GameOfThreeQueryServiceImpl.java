package com.takeaway.gameofthree.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.takeaway.gameofthree.domain.entity.Game;
import com.takeaway.gameofthree.enums.GameStatus;
import com.takeaway.gameofthree.repository.GameRepository;
import com.takeaway.gameofthree.service.GameOfThreeQueryService;

@Service
@Transactional(readOnly = true)
public class GameOfThreeQueryServiceImpl implements GameOfThreeQueryService {

	@Autowired
	private GameRepository gameRepository;
	
	@Override
	public List<Game> retriveWaitingGames() {
		return gameRepository.findAll().stream().filter(e -> (e.getGameStatus().equals(GameStatus.NOT_STARTED)
				|| e.getGameStatus().equals(GameStatus.WAITING_FOR_PLAYER))).collect(Collectors.toList());
	}

	@Override
	public Optional<Game> retrieveTheGameById(Long gameId) {
		return gameRepository.findById(gameId);
	}

}
