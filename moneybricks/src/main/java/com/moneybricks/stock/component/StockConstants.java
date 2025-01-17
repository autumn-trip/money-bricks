package com.moneybricks.stock.component;

import com.moneybricks.stock.domain.Stock;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class StockConstants {
    public static final Map<String, List<Stock>> INDUSTRY_STOCKS = Map.of(
            "IT/반도체", Arrays.asList(
                    new Stock("005930", "삼성전자", "IT/반도체"),
                    new Stock("000660", "SK하이닉스", "IT/반도체"),
                    new Stock("035420", "NAVER", "IT/반도체"),
                    new Stock("035720", "카카오", "IT/반도체"),
                    new Stock("066570", "LG전자", "IT/반도체")
            ),
            "자동차", Arrays.asList(
                    new Stock("005380", "현대차", "자동차"),
                    new Stock("000270", "기아", "자동차"),
                    new Stock("012330", "현대모비스", "자동차"),
                    new Stock("011210", "현대위아", "자동차"),
                    new Stock("023800", "인지컨트롤스", "자동차")
            ),
            "건설", Arrays.asList(
                    new Stock("000720", "현대건설", "건설"),
                    new Stock("028050", "삼성엔지니어링", "건설"),
                    new Stock("047040", "대우건설", "건설"),
                    new Stock("006360", "GS건설", "건설"),
                    new Stock("002630", "오뚜기", "건설")
            ),
            "증권", Arrays.asList(
                    new Stock("005940", "NH투자증권", "증권"),
                    new Stock("039490", "키움증권", "증권"),
                    new Stock("078020", "대신증권", "증권"),
                    new Stock("001500", "현대차증권", "증권"),
                    new Stock("003550", "LG", "증권")
            ),
            "제약", Arrays.asList(
                    new Stock("068270", "셀트리온", "제약"),
                    new Stock("207940", "삼성바이오로직스", "제약"),
                    new Stock("128940", "한미약품", "제약"),
                    new Stock("302440", "SK바이오사이언스", "제약"),
                    new Stock("068760", "셀트리온제약", "제약")
            )
    );
}