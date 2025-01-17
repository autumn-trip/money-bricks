//package com.moneybricks.stock.config;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class WebSocketHandler extends TextWebSocketHandler {
//    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) {
//        String userId = extractUserId(session);
//        sessions.put(userId, session);
//    }
//
//    public void sendMessage(String userId, NotificationMessage message) {
//        WebSocketSession session = sessions.get(userId);
//        if (session != null && session.isOpen()) {
//            try {
//                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
//            } catch (IOException e) {
//                log.error("Failed to send message", e);
//            }
//        }
//    }
//}