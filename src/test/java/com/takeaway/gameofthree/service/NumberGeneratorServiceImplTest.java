package com.takeaway.gameofthree.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.takeaway.gameofthree.service.impl.NumberGenaratorServiceImpl;
@RunWith(SpringJUnit4ClassRunner.class)
public class NumberGeneratorServiceImplTest {

    @InjectMocks
    NumberGenaratorServiceImpl numberGenaratorServiceImpl;
    
    @Test
    public void testGenerateRandomNumber()
    {
        assertNotNull(numberGenaratorServiceImpl.generateRandomNumber());
    }
    
    @Test
    public void testGenerateRandomNumberRange()
    {
        assertNotNull(numberGenaratorServiceImpl.generateRandomNumber(100));
    }
}
