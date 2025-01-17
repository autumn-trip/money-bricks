package com.moneybricks.stock.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Id
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String industry;

    @OneToMany(mappedBy = "stock")
    private List<StockPrice> priceHistory;

    public Stock(String code, String name, String industry) {
        this.code = code;
        this.name = name;
        this.industry = industry;
    }
}