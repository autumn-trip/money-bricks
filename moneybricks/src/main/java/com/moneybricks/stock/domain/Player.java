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
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate gameStartDate;

    @Column(nullable = false)
    private LocalDate currentDate;

    @Column(nullable = false)
    private double cash;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<PlayerStock> stocks;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<TradeHistory> tradeHistory;
}
