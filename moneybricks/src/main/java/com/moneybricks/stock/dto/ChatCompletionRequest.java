package com.moneybricks.stock.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
@Builder
public class ChatCompletionRequest {
    private String model;
    private List<Message> messages;
    private double temperature;

    @Getter
    @Builder
    public static class ChatMessage {
        private String role;
        private String content;
    }
}
