package com.arthur.sportsclubscoringsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clubes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clube {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private LocalDate dataFundacao;

    private String dono;

    private Double pontuacaoTotal = 0.0;


    @OneToMany
    private List<Participacao> participacoes = new ArrayList<>();
}