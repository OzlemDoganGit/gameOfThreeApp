package com.takeaway.gameofthree.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.InjectMocks;

import com.takeaway.gameofthree.enums.ValidationStatus;

public class ValidationNumberTest {
    
    @InjectMocks
    ValidatedNumber validatedNumber;
    
    @Test
    public void testBuildOfValidatedNumber()
    {
        ValidatedNumber validNumb = ValidatedNumber.builder().move(1).number(55).status(ValidationStatus.NOT_VALID).build();
        
        assertNotNull(validNumb);
        
    }
    

}
