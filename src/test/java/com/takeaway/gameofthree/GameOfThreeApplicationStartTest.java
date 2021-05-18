package com.takeaway.gameofthree;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GameOfThreeApplication.class)
public class GameOfThreeApplicationStartTest {

    @Test
    public void applicationStarts() {
        GameOfThreeApplication.main(new String[] {});
    }

}
