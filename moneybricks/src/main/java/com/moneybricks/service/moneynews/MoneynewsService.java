package com.moneybricks.service.moneynews;

import com.moneybricks.dto.MoneynewsDTO;
import java.util.List;

public interface MoneynewsService {
    List<MoneynewsDTO> getMoneyNews(String query, int page, int size);
}
