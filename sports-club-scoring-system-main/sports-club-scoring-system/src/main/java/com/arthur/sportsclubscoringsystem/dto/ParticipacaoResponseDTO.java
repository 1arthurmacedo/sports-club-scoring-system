package com.arthur.sportsclubscoringsystem.dto;
import com.arthur.sportsclubscoringsystem.enums.Posicao;
import com.arthur.sportsclubscoringsystem.model.Campeonato;
import com.arthur.sportsclubscoringsystem.model.Clube;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipacaoResponseDTO {

    private Long id;
    private String nomeClube;
    private String nomeCampeonato;
    @Enumerated(EnumType.STRING)
    private Posicao posicao;
}
