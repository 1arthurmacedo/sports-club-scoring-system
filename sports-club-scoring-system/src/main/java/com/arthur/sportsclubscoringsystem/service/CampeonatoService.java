package com.arthur.sportsclubscoringsystem.service;

import com.arthur.sportsclubscoringsystem.dto.CampeonatoRequestDTO;
import com.arthur.sportsclubscoringsystem.dto.CampeonatoResponseDTO;
import com.arthur.sportsclubscoringsystem.dto.ParticipacaoRequestDTO;
import com.arthur.sportsclubscoringsystem.dto.ParticipacaoResponseResumoCampeonatoDTO;
import com.arthur.sportsclubscoringsystem.enums.Posicao;
import com.arthur.sportsclubscoringsystem.model.Campeonato;
import com.arthur.sportsclubscoringsystem.model.Clube;
import com.arthur.sportsclubscoringsystem.model.Participacao;
import com.arthur.sportsclubscoringsystem.repository.CampeonatoRepository;
import com.arthur.sportsclubscoringsystem.repository.ClubeRepository;
import com.arthur.sportsclubscoringsystem.repository.ParticipacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CampeonatoService {

    private final CampeonatoRepository campeonatoRepository;
    private final ParticipacaoRepository participacaoRepository;
    private final ClubeRepository clubeRepository;

    public CampeonatoService(CampeonatoRepository campeonatoRepository, ParticipacaoRepository participacaoRepository, ClubeRepository clubeRepository) {
        this.campeonatoRepository = campeonatoRepository;
        this.participacaoRepository = participacaoRepository;
        this.clubeRepository = clubeRepository;
    }

    public CampeonatoResponseDTO salvar(CampeonatoRequestDTO dto) {

        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do campeonato é obrigatório.");
        }

        if (dto.getNivel() == null)  {
            throw new IllegalArgumentException("O nível do campeonato é obrigatório.");
        }

        if (dto.getDataInicio() == null || dto.getDataFim() == null) {
            throw new IllegalArgumentException("Data inválida.");
        } else if (dto.getDataInicio().isAfter(dto.getDataFim())) {
            throw new IllegalArgumentException("Data inválida.");
        }

        Campeonato campeonato = new Campeonato();

        campeonato.setNome(dto.getNome());
        campeonato.setNivel(dto.getNivel());
        campeonato.setDataInicio(dto.getDataInicio());
        campeonato.setDataFim(dto.getDataFim());

        campeonatoRepository.salvar(campeonato);
        return new CampeonatoResponseDTO(
                campeonato.getId(),
                campeonato.getNome(),
                campeonato.getDataInicio(),
                campeonato.getDataFim(),
                campeonato.getNivel(),
                new ArrayList<>()
        );
    }

    public CampeonatoResponseDTO buscarPorId(Long id) {
        Campeonato campeonato = campeonatoRepository.buscarPorId(id);

        if (campeonato == null) {
            return null;
        }

        return new CampeonatoResponseDTO(
                campeonato.getId(),
                campeonato.getNome(),
                campeonato.getDataInicio(),
                campeonato.getDataFim(),
                campeonato.getNivel(),
                campeonato.getResultados()
                        .stream()
                        .map(participacao -> new ParticipacaoResponseResumoCampeonatoDTO(
                                participacao.getClube().getNome(),
                                participacao.getPosicao()
                        )).toList()
        );
    }

    public List<CampeonatoResponseDTO> buscarTodos() {
        return campeonatoRepository.buscarTodos()
                .stream()
                .map(campeonato -> new CampeonatoResponseDTO(
                        campeonato.getId(),
                        campeonato.getNome(),
                        campeonato.getDataInicio(),
                        campeonato.getDataFim(),
                        campeonato.getNivel(),
                        campeonato.getResultados()
                                .stream()
                                .map(participacao -> new ParticipacaoResponseResumoCampeonatoDTO(
                                        participacao.getClube().getNome(),
                                        participacao.getPosicao()
                                )).toList()
                )).toList();
    }

    public CampeonatoResponseDTO atualizar(Long id, CampeonatoRequestDTO dto) {
        Campeonato campeonatoExistente = campeonatoRepository.buscarPorId(id);

        if (campeonatoExistente == null) {
            throw new IllegalArgumentException("Não existe esse campeonato.");
        }

        campeonatoExistente.setNome(dto.getNome());
        campeonatoExistente.setDataInicio(dto.getDataInicio());
        campeonatoExistente.setDataFim(dto.getDataFim());
        if (dto.getResultados() != null) {
            campeonatoExistente.setResultados(TransformarResponseClube(dto));
        }
        campeonatoExistente.setNivel(dto.getNivel());
        campeonatoRepository.atualizar(campeonatoExistente);

        ParticipacaoService participacaoService = new ParticipacaoService(participacaoRepository, campeonatoRepository, clubeRepository);
        List<Participacao> participacaos = new ArrayList<>(campeonatoExistente.getResultados());
        for (Participacao participacao : participacaos) {
            participacao.setCampeonato(campeonatoExistente);
            participacaoService.atualizar(participacao.getId(), new ParticipacaoRequestDTO(
                    participacao.getClube().getId(),
                    campeonatoExistente.getId(),
                    participacao.getPosicao()
            ));
        }
        return new CampeonatoResponseDTO(
                campeonatoExistente.getId(),
                campeonatoExistente.getNome(),
                campeonatoExistente.getDataInicio(),
                campeonatoExistente.getDataFim(),
                campeonatoExistente.getNivel(),
                campeonatoExistente.getResultados().stream().map(
                        participacao -> new ParticipacaoResponseResumoCampeonatoDTO(
                                participacao.getClube().getNome(),
                                participacao.getPosicao()
                        )).toList()
        );
    }

    public void atualizarResultados(Long id, List<Participacao> resultados) {
        if (id == null) {
            throw new IllegalArgumentException("ID inválido.");
        }
        int contador = 0;
        for (Participacao resultado : resultados) {
            if (resultado.getPosicao() == Posicao.PRIMEIRO || resultado.getPosicao() == Posicao.SEGUNDO || resultado.getPosicao() == Posicao.TERCEIRO) {
                contador += 1;
            }
        }
        if (contador > 3) {
            throw new IllegalArgumentException("Pódio inválido.");
        }
        Campeonato campeonatoExistente = campeonatoRepository.buscarPorId(id);

        if (campeonatoExistente == null) {
            throw new IllegalArgumentException("Clube não existe.");
        }

        campeonatoExistente.setResultados(resultados);
        campeonatoRepository.atualizar(campeonatoExistente);
    }

    public void excluir(Long id) {
        Campeonato campeonatoExistente = campeonatoRepository.buscarPorId(id);

        if(campeonatoExistente == null) {
            throw new IllegalArgumentException("Campeonato inválido.");
        }

        ParticipacaoService participacaoService = new ParticipacaoService(participacaoRepository, campeonatoRepository, clubeRepository);
        List<Participacao> resultados = new ArrayList<>(campeonatoExistente.getResultados());
        for (Participacao participacao : resultados) {
            participacaoService.excluir(participacao.getId());
        }

        campeonatoRepository.excluir(campeonatoExistente.getId());
    }

    public List<Participacao> TransformarResponseClube(CampeonatoRequestDTO dto) {
        List<Clube> clubes = new ArrayList<>();

        for (ParticipacaoResponseResumoCampeonatoDTO clubeResumo : dto.getResultados()) {
            Clube clube = clubeRepository.buscarPorNome(clubeResumo.getNomeClube());
            if (clube == null) {
                throw new IllegalArgumentException("Clube não encontrado: " + clubeResumo.getNomeClube());
            }
            clubes.add(clube);
        }

        List<Participacao> participacoes = new ArrayList<>();

        for (Clube clube : clubes) {
            for (Participacao participacao : clube.getParticipacoes()) {
                if (Objects.equals(participacao.getCampeonato().getNome(), dto.getNome())){
                    participacoes.add(participacao);
                }
            }
        }

        return participacoes;
    }


}