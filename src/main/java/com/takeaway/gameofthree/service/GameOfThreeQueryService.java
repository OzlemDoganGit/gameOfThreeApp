package com.takeaway.gameofthree.service;

import java.util.List;
import java.util.Optional;

import com.takeaway.gameofthree.domain.entity.Game;

public interface GameOfThreeQueryService {

    public List<Game> retriveWaitingGames();

    public Optional<Game> retrieveTheGameById(Long gameId);

}
