package com.arthur.sportsclubscoringsystem.model;

import com.arthur.sportsclubscoringsystem.enums.Nivel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campeonatos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campeonato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    private Nivel nivel;

    @OneToMany
    private List<Participacao> resultados = new ArrayList<>();
}