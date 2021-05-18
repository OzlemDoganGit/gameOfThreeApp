package com.takeaway.gameofthree.service.impl;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.takeaway.gameofthree.service.NumberGeneratorService;

@Component("randomNumberGeneratorService")
public class NumberGenaratorServiceImpl implements NumberGeneratorService {
    private static final int MINIMUM = 3; // every number should be divided by 3.
    private static final int DEFAULTNUMBER = 100;

    @Override
    public Integer generateRandomNumber() {
        return new Random().nextInt(DEFAULTNUMBER - MINIMUM) + MINIMUM;
    }

    @Override
    public Integer generateRandomNumber(Integer range) {
        return new Random().nextInt(range - MINIMUM + 1) + MINIMUM;
    }

}
