package com.takeaway.gameofthree.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.takeaway.gameofthree.enums.ArithmeticOperator;

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
public class Point {

	@Id
	@GeneratedValue
	private int id;

	@Column(nullable = true)
	Integer startedNumber;

	@Column(nullable = true)
	Integer updatedNumber;

	@Column(nullable = true)
	ArithmeticOperator operator;

	@Column(nullable = true)
	Integer adjustedNumber;

	@Column(nullable = true)
	Integer arithmeticNumber;

}
