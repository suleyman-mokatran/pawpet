package com.company.pawpet.notification;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationHandler extends TextWebSocketHandler {

    private static final Map<Integer, WebSocketSession> userSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String query = session.getUri().getQuery();
        System.out.println("New WebSocket connection request with query: " + query);

        if (query != null) {
             if (query.startsWith("userId=")) {
                try {
                    int userId = Integer.parseInt(query.split("=")[1]);
                    userSessions.put(userId, session);
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid userId format in query: " + query);
                }
            }
            else {
                System.out.println("❌ No userIds in query: " + query);
            }
        } else {
            System.out.println("❌ No query string found in WebSocket request.");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        System.out.println("Received: " + message.getPayload());
        if (session.isOpen()) {
            session.sendMessage(new TextMessage("Server received: " + message.getPayload()));
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        userSessions.values().remove(session);
    }


    public void sendNotificationToUser(int userId, String message) throws IOException {
        System.out.println("sendNotificationToDoctor CALLED for userId: " + userId + " with message: " + message);

        WebSocketSession session = userSessions.get(userId);
        boolean sessionActive = session != null && session.isOpen();
        System.out.println("Checking session status for user " + userId + ": " + sessionActive);

        if (sessionActive) {
            session.sendMessage(new TextMessage(message));
            System.out.println("✅ Sent message: " + message);
        } else {
            System.out.println("❌ No active WebSocket session found for userId " + userId);
            System.out.println("❌ Message NOT stored. Missed notifications are disabled.");
        }
    }
}
