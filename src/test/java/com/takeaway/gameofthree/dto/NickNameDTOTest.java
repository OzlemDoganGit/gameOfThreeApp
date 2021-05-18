package com.takeaway.gameofthree.dto;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class NickNameDTOTest {

    @InjectMocks
    NickNameDTO nickName;

    @Test
    public void testNickName() {
        nickName.setPlayerNickName("OZLEM");
        Assert.assertEquals("OZLEM", nickName.getPlayerNickName());

    }

    @Test
    public void testEquals() {
        NickNameDTO nickNameDTO_first = new NickNameDTO();
        NickNameDTO nickNameDTO_second = new NickNameDTO();
        boolean result = nickNameDTO_first.equals(nickNameDTO_second);
        assertTrue(result);
    }
    
    @Test
    public void testHashCode() {
        NickNameDTO nickNameDTO_first = new NickNameDTO();
        NickNameDTO nickNameDTO_second = new NickNameDTO();
        Integer hashCodeFirst = nickNameDTO_first.hashCode();
        Integer hashCodeSecond = nickNameDTO_second.hashCode();
        Assert.assertEquals( hashCodeFirst, hashCodeSecond);
    }

}
