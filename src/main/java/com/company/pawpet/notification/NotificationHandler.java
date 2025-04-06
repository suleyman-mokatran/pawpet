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

    private static final Map<Integer, WebSocketSession> doctorSessions = new HashMap<>();

    private static final Map<Integer, WebSocketSession> userSessions = new HashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String query = session.getUri().getQuery();
        System.out.println("New WebSocket connection request with query: " + query);

        if (query != null) {
            if (query.startsWith("doctorId=")) {
                try {
                    int doctorId = Integer.parseInt(query.split("=")[1]);
                    doctorSessions.put(doctorId, session);
                    System.out.println("✅ Doctor " + doctorId + " WebSocket session stored!");
                    System.out.println("Current doctorSessions: " + doctorSessions.keySet());
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid doctorId format in query: " + query);
                }
            } else if (query.startsWith("userId=")) {
                try {
                    int userId = Integer.parseInt(query.split("=")[1]);
                    userSessions.put(userId, session);
                    System.out.println("✅ User " + userId + " WebSocket session stored!");
                    System.out.println("Current userSessions: " + userSessions.keySet());
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid userId format in query: " + query);
                }
            } else {
                System.out.println("❌ Neither doctorId nor userId found in query: " + query);
            }
        } else {
            System.out.println("❌ No query string found in WebSocket request.");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        System.out.println("Received: " + message.getPayload());

        // Optional: echo or broadcast message (you can remove this if not needed)
        for (WebSocketSession s : doctorSessions.values()) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage("Server received: " + message.getPayload()));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        doctorSessions.values().remove(session);  // Remove the session when it closes
        System.out.println("❌ WebSocket closed for doctor. Current doctorSessions: " + doctorSessions.keySet());
    }

    public void sendNotificationToDoctor(int doctorId, String message) throws IOException {
        System.out.println("sendNotificationToDoctor CALLED for doctorId: " + doctorId + " with message: " + message);

        WebSocketSession session = doctorSessions.get(doctorId);
        boolean sessionActive = session != null && session.isOpen();
        System.out.println("Checking session status for doctor " + doctorId + ": " + sessionActive);

        if (sessionActive) {
            session.sendMessage(new TextMessage(message));
            System.out.println("✅ Sent message: " + message);
        } else {
            System.out.println("❌ No active WebSocket session found for doctorId " + doctorId);
            System.out.println("❌ Message NOT stored. Missed notifications are disabled.");
        }
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
