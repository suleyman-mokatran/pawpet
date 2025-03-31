package com.company.pawpet.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@Component // Register as a Spring Bean
public class NotificationHandler extends TextWebSocketHandler {

    private static final Map<Integer, WebSocketSession> doctorSessions = new HashMap<>();

    private final Map<Integer, Queue<String>> pendingNotifications = new HashMap<>(); // Store missed messages


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String query = session.getUri().getQuery();
        System.out.println("New WebSocket connection request with query: " + query);

        if (query != null && query.startsWith("doctorId=")) {
            try {
                int doctorId = Integer.parseInt(query.split("=")[1]);
                doctorSessions.put(doctorId, session);
                System.out.println("✅ Doctor " + doctorId + " WebSocket session stored!");
                System.out.println("Current doctorSessions: " + doctorSessions.keySet());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid doctorId format in query: " + query);
            }
        } else {
            System.out.println("❌ doctorId not found in query: " + query);
        }
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        System.out.println("Received: " + message.getPayload());

        // If the message is a request for missed notifications
        if (message.getPayload().equals("{\"type\":\"request_missed_notifications\"}")) {
            int doctorId = getDoctorIdFromSession(session); // Retrieve doctorId from the session
            List<String> missedNotifications = getMissedNotificationsForDoctor(doctorId); // Get missed notifications for doctor

            // Create a response with missed notifications
            Map<String, Object> response = new HashMap<>();
            response.put("type", "missed_notifications");
            response.put("notifications", missedNotifications);

            String jsonResponse = new ObjectMapper().writeValueAsString(response);

            // Send the missed notifications back to the client
            session.sendMessage(new TextMessage(jsonResponse));
            System.out.println("✅ Sent missed notifications: " + missedNotifications);
        } else {
            // For other messages, broadcast the received message
            for (WebSocketSession s : doctorSessions.values()) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage("Server received: " + message.getPayload()));
                }
            }
        }
    }

    // Dummy method to retrieve missed notifications for a specific doctor
    private List<String> getMissedNotificationsForDoctor(int doctorId) {
        // Replace with actual logic to fetch missed notifications
        return Arrays.asList("New appointment scheduled!");
    }

    // Dummy method to extract doctorId from session
    private int getDoctorIdFromSession(WebSocketSession session) {
        // You can extract the doctorId from session attributes or any other logic
        return 2; // Example: return the doctorId as 2 for now
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
            System.out.println("❌ No active WebSocket session found for doctorId: " + doctorId);

            // Store message for later delivery
            pendingNotifications.putIfAbsent(doctorId, new LinkedList<>());
            pendingNotifications.get(doctorId).add(message);
            System.out.println("📌 Message stored for doctorId " + doctorId);
        }
    }

    public void storeDoctorSession(int doctorId, WebSocketSession session) throws IOException {
        System.out.println("New WebSocket connection request with query: doctorId=" + doctorId);
        doctorSessions.put(doctorId, session);
        System.out.println("✅ Doctor " + doctorId + " WebSocket session stored!");
        System.out.println("Current doctorSessions: " + doctorSessions.keySet());

        // Send pending messages if any
        if (pendingNotifications.containsKey(doctorId)) {
            Queue<String> messages = pendingNotifications.get(doctorId);
            while (!messages.isEmpty()) {
                String pendingMessage = messages.poll();
                session.sendMessage(new TextMessage(pendingMessage));
                System.out.println("📩 Sent stored message to doctorId " + doctorId + ": " + pendingMessage);
            }
            pendingNotifications.remove(doctorId);
        }
    }

    public void handleIncomingMessage(int doctorId, String message, WebSocketSession session) throws IOException {
        System.out.println("📩 Received message from doctorId " + doctorId + ": " + message);

        // Parse message (assuming JSON format)
        if (message.contains("\"type\":\"request_missed_notifications\"")) {
            sendMissedNotifications(doctorId, session);
        }
    }

    private void sendMissedNotifications(int doctorId, WebSocketSession session) throws IOException {
        if (pendingNotifications.containsKey(doctorId)) {
            Queue<String> messages = pendingNotifications.get(doctorId);
            while (!messages.isEmpty()) {
                String pendingMessage = messages.poll();
                session.sendMessage(new TextMessage(pendingMessage));
                System.out.println("📩 Sent stored message to doctorId " + doctorId + ": " + pendingMessage);
            }
            pendingNotifications.remove(doctorId);
        } else {
            System.out.println("✅ No missed notifications for doctorId " + doctorId);
        }
    }

}
