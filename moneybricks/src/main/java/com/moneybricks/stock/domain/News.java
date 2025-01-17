package com.moneybricks.stock.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private double sentimentScore;

    @ManyToMany
    @JoinTable(
            name = "news_affected_stocks",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "stock_code")
    )
    private List<Stock> affectedStocks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NewsType type;  // MARKET, INDUSTRY, COMPANY
}