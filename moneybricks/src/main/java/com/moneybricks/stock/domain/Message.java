package com.moneybricks.stock.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String role;    // system, user, assistant 등
    private String content; // 실제 메시지 내용
}