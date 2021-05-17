package com.takeaway.gameofthree.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.takeaway.gameofthree.enums.PlayerStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Player {
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable = true)
	String name;

	@Column(nullable = true)
    private PlayerStatus playerStatus;

}
