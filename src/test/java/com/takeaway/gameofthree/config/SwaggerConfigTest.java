package com.takeaway.gameofthree.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class SwaggerConfigTest {

    @InjectMocks
    private SwaggerConfig config;

    @Test
    public void swaggerTest() {

        assertNotNull(config.apiDoc());
    }

}
