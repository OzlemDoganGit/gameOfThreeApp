package com.takeaway.gameofthree.service.impl;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.takeaway.gameofthree.service.NumberGeneratorService;

@Service("randomNumberGeneratorService")
public class NumberGenaratorServiceImpl implements NumberGeneratorService {
	final int minimum = 3; // every number should be divided by 3.
	final int defaultNumber = 100;

	@Override
	public Integer generateRandomNumber() {
		Random rand = new Random();

		int randomIntger = rand.nextInt(defaultNumber - minimum) + minimum;
		return Integer.valueOf(randomIntger);
	}

	@Override
	public Integer generateRandomNumber(Integer range) {
		Random rand = new Random();
		int randomIntger = rand.nextInt(range - minimum + 1) + minimum;
		return Integer.valueOf(randomIntger);
	}

}
