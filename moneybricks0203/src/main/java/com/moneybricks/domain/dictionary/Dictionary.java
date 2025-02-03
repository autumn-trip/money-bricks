package com.moneybricks.domain.dictionary;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Dictionary")public class Dictionary {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long dictionaryId;

        @Column(nullable = false)
        private String dictionarySort;

        @Column(nullable = false)
        private String dictionaryTerms;

        @Column( name = "dictionary_definitions", columnDefinition = "TEXT", nullable = false)
        private String dictionaryDefinitions;

        @Column(name = "dictionary_code",nullable = false)
        private Integer dictionaryCode; // 1: 금융, 2: 경제, 3: 경영 ......
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private Integer code; //
    }


