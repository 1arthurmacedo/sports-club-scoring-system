package com.arthur.sportsclubscoringsystem.model;

import com.arthur.sportsclubscoringsystem.enums.Posicao;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "participacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clube_id")
    private Clube clube;

    @ManyToOne
    @JoinColumn(name = "campeonato_id")
    private Campeonato campeonato;

    @Enumerated(EnumType.STRING)
    private Posicao posicao;

}