package com.moneybricks.stock.dto;

import lombok.Data;

import java.util.Map;

@Data
public class KisApiResponse {
    private Map<String, String> output;
    private String rt_cd;
    private String msg_cd;
    private String msg1;
}
