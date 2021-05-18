package com.takeaway.gameofthree.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.takeaway.gameofthree.domain.entity.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

}
