package com.arthur.sportsclubscoringsystem.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubeResponseDTO {

    private Long id;
    private String nome;
    private LocalDate dataFundacao;
    private String dono;
    private Double pontuacaoTotal;
    private List<ParticipacaoResponseResumoClubeDTO> participacoes;

}
