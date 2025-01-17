package com.moneybricks.stock.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_code")
    private Stock stock;

    @Column(nullable = false)
    private LocalDateTime tradeDate;

    @Enumerated(EnumType.STRING)
    private TradeType type;  // BUY, SELL

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;
}