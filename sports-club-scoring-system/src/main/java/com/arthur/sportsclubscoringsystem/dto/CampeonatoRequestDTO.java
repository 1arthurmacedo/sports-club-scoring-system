package com.arthur.sportsclubscoringsystem.dto;

import com.arthur.sportsclubscoringsystem.enums.Nivel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampeonatoRequestDTO {

    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Nivel nivel;

    private List<ParticipacaoResponseResumoCampeonatoDTO> resultados;

    // sem resultados
    public CampeonatoRequestDTO(
            String nome,
            LocalDate dataInicio,
            LocalDate dataFim,
            Nivel nivel) {

        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.nivel = nivel;
        this.resultados = new ArrayList<>();
    }

    // com resultados
    public CampeonatoRequestDTO(String nome, LocalDate dataInicio, LocalDate dataFim, Nivel nivel, List<ParticipacaoResponseResumoCampeonatoDTO> resultados) {

        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.nivel = nivel;
        this.resultados = resultados != null
                ? resultados
                : new ArrayList<>();
    }
}
