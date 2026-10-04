package com.arthur.sportsclubscoringsystem.service;

import com.arthur.sportsclubscoringsystem.dto.*;
import com.arthur.sportsclubscoringsystem.model.Clube;
import com.arthur.sportsclubscoringsystem.model.Participacao;
import com.arthur.sportsclubscoringsystem.repository.ClubeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ClubeService {
    private final ClubeRepository clubeRepository;

    public ClubeService(ClubeRepository clubeRepository) { this.clubeRepository = clubeRepository; }

    public ClubeResponseDTO salvar(ClubeRequestDTO dto) {

        if (dto.getDono() == null || dto.getDono().isBlank()) {
            throw new IllegalArgumentException("O nome do dono é obrigatório.");
        }

        if (dto.getDataFundacao() == null || dto.getDataFundacao().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data inválida.");
        }

        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do clube é obrigatório.");
        }

        Clube clube = new Clube();

        clube.setDono(dto.getDono());
        clube.setDataFundacao(dto.getDataFundacao());
        clube.setNome(dto.getNome());
        clube.setParticipacoes(new ArrayList<Participacao>());
        clube.setPontuacaoTotal(0.0);

        clubeRepository.salvar(clube);

        return new ClubeResponseDTO(
                clube.getId(),
                clube.getNome(),
                clube.getDataFundacao(),
                clube.getDono(),
                clube.getPontuacaoTotal(),
                new ArrayList<>()
        );
    }

    public ClubeResponseDTO buscarPorId(Long id) {
        Clube clube = clubeRepository.buscarPorId(id);

        if (clube == null) {
            return null;
        }

        return new ClubeResponseDTO(
                clube.getId(),
                clube.getNome(),
                clube.getDataFundacao(),
                clube.getDono(),
                clube.getPontuacaoTotal(),
                (List<ParticipacaoResponseResumoClubeDTO>) clube.getParticipacoes()
                        .stream()
                        .map(
                                participacao -> new ParticipacaoResponseResumoClubeDTO(
                                        participacao.getCampeonato().getNome(),
                                        participacao.getPosicao())
                        ).toList()
        );
    }

    public ClubeResponseDTO atualizar(Long id, ClubeRequestDTO dto) {
        Clube clubeExistente = clubeRepository.buscarPorId(id);

        if (clubeExistente == null) {
            throw new IllegalArgumentException("Clube não existe.");
        }

        clubeExistente.setNome(dto.getNome());
        clubeExistente.setDono(dto.getDono());
        clubeExistente.setDataFundacao(dto.getDataFundacao());

        clubeRepository.atualizar(clubeExistente);

        return new ClubeResponseDTO(
                clubeExistente.getId(),
                clubeExistente.getNome(),
                clubeExistente.getDataFundacao(),
                clubeExistente.getDono(),
                clubeExistente.getPontuacaoTotal(),
                clubeExistente.getParticipacoes().stream()
                        .map(participacao -> new ParticipacaoResponseResumoClubeDTO(
                                participacao.getCampeonato().getNome(),
                                participacao.getPosicao()
                        )).toList()
        );
    }

    public void atualizarPontosEResultados(Long id, Double pontos, Participacao participacao) {
        if (id == null) {
            throw new IllegalArgumentException("ID inválido.");
        }

        Clube clubeExistente = clubeRepository.buscarPorId(id);

        if (clubeExistente == null) {
            throw new IllegalArgumentException("Clube não existe.");
        }

        List<Participacao> participacaos = clubeExistente.getParticipacoes();
        participacaos.add(participacao);

        clubeExistente.setParticipacoes(participacaos);
        clubeExistente.setPontuacaoTotal(clubeExistente.getPontuacaoTotal() + pontos);
        clubeRepository.atualizar(clubeExistente);
    }

    public List<ClubeResponseDTO> buscarTodos() {
        return clubeRepository.buscarTodos()
                .stream()
                .map(clube -> new ClubeResponseDTO(
                        clube.getId(),
                        clube.getNome(),
                        clube.getDataFundacao(),
                        clube.getDono(),
                        clube.getPontuacaoTotal(),
                        clube.getParticipacoes()
                                .stream()
                                .map(participacao -> new ParticipacaoResponseResumoClubeDTO(
                                        participacao.getCampeonato().getNome(),
                                        participacao.getPosicao()
                                )).toList()
                )).toList();
    }

    public void excluir(Long id) {
        Clube clube = clubeRepository.buscarPorId(id);

        if(clube == null) {
            throw new IllegalArgumentException("Clube não encontrado.");
        }

        clubeRepository.excluir(id);
    }
}