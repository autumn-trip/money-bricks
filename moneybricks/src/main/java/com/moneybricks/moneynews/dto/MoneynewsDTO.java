package com.moneybricks.moneynews.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MoneynewsDTO {
    private String title;
    private String description;
    private String originallink;
    private String pubDate;
}
