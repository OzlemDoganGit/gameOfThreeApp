package com.takeaway.gameofthree.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.takeaway.gameofthree.enums.ValidationStatus;

@RunWith(SpringJUnit4ClassRunner.class)
public class ValidationNumberTest {

    @InjectMocks
    ValidatedNumber validatedNumber;

    @Test
    public void testBuildOfValidatedNumber() {
        ValidatedNumber validNumb = ValidatedNumber.builder().move(1).number(55).status(ValidationStatus.NOT_VALID)
                .build();

        assertNotNull(validNumb);

    }

    @Test
    public void testSetMove() {
        validatedNumber.setMove(5);
        assertEquals(Integer.valueOf(5), validatedNumber.getMove());
    }

    @Test
    public void testSetNumber() {
        validatedNumber.setNumber(5);
        assertEquals(Integer.valueOf(5), validatedNumber.getNumber());
    }

    @Test
    public void testSetValidationStatus() {
        validatedNumber.setStatus(ValidationStatus.VALID);
        assertEquals(ValidationStatus.VALID, validatedNumber.getStatus());
    }
    

}
