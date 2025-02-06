package com.moneybricks.moneynews.service;

import com.moneybricks.moneynews.dto.MoneynewsDTO;
import java.util.List;

public interface MoneynewsService {
    List<MoneynewsDTO> getMoneyNews(String query, int page, int size);
}
