package com.moneybricks.stock.dto;

import lombok.Data;

@Data
public class KisTokenResponse {
    private String access_token;
    private int expires_in;
}
