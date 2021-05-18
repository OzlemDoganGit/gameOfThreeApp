package com.takeaway.gameofthree.controller;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.takeaway.gameofthree.GameOfThreeApplication;

@RunWith(SpringJUnit4ClassRunner.class)
public class WebSocketEventListenerTest {

    @InjectMocks
    WebSocketEventListener webSocketEventListener;

    @Mock
    SessionConnectedEvent sessionConnectedEvent;

    @Mock
    SessionDisconnectEvent sessionDisconnectedEvent;

    @Test
    public void testHandleWebSocketConnectListener() {
        webSocketEventListener.handleWebSocketConnectListener(sessionConnectedEvent);
        assertTrue(true);
    }

    @Test
    public void handleWebSocketDisconnectListener() {
        webSocketEventListener.handleWebSocketDisconnectListener(sessionDisconnectedEvent);
        assertTrue(true);
    }

}
