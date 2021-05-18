package com.takeaway.gameofthree.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.takeaway.gameofthree.domain.entity.Point;
import com.takeaway.gameofthree.enums.ArithmeticOperator;

@RunWith(SpringJUnit4ClassRunner.class)
public class PointTest {
    
    @InjectMocks
    Point point;

    @Test
    public void testBuildPoint() {
        Point point = Point.builder().arithmeticNumber(3).operator(ArithmeticOperator.DIVIDE).startedNumber(55)
                .updatedNumber(55).build();
        assertNotNull(point);
    }
    
    @Test
    public void testSetArithmeticNumber() {
        point.setArithmeticNumber(5);
        assertEquals(Integer.valueOf(5), point.getArithmeticNumber());
    }
    
    @Test
    public void testSetID() {
        point.setId(5);
        assertEquals(5, point.getId());
    }
    @Test
    public void testSetStartedNumber() {
        point.setStartedNumber(5);
        assertEquals(Integer.valueOf(5), point.getStartedNumber());
    }
    
    @Test
    public void testSetUpdatedNumber() {
        point.setUpdatedNumber(5);
        assertEquals(Integer.valueOf(5), point.getUpdatedNumber());
    }
    
    @Test
    public void testSetAdjustmentNumber() {
        point.setAdjustedNumber(5);
        assertEquals(Integer.valueOf(5), point.getAdjustedNumber());
    }
    
    @Test
    public void testSetArithmeticOperator() {
        point.setOperator(ArithmeticOperator.DIVIDE);
        assertEquals(ArithmeticOperator.DIVIDE, point.getOperator());
    }
}
