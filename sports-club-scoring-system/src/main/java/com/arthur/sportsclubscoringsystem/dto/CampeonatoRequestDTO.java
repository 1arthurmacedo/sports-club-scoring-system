package com.arthur.sportsclubscoringsystem.dto;

import com.arthur.sportsclubscoringsystem.enums.Nivel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampeonatoRequestDTO {

    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Nivel nivel;
    private List<ParticipacaoResponseResumoCampeonatoDTO> resultados;
}