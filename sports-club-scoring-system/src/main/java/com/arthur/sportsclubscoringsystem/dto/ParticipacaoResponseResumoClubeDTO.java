package com.arthur.sportsclubscoringsystem.dto;
import com.arthur.sportsclubscoringsystem.enums.Posicao;
import com.arthur.sportsclubscoringsystem.model.Campeonato;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipacaoResponseResumoClubeDTO {

    private String nomeCampeonato;
    @Enumerated(EnumType.STRING)
    private Posicao posicao;

}
