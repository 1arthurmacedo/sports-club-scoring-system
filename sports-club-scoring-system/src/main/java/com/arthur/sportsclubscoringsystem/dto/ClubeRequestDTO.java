package com.arthur.sportsclubscoringsystem.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubeRequestDTO {
    private String nome;
    private LocalDate dataFundacao;
    private String dono;
}
