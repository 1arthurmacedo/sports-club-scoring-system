package com.arthur.sportsclubscoringsystem.service;

import com.arthur.sportsclubscoringsystem.dto.ClubeRequestDTO;
import com.arthur.sportsclubscoringsystem.dto.ParticipacaoRequestDTO;
import com.arthur.sportsclubscoringsystem.dto.ParticipacaoResponseDTO;
import com.arthur.sportsclubscoringsystem.enums.Nivel;
import com.arthur.sportsclubscoringsystem.enums.Posicao;
import com.arthur.sportsclubscoringsystem.model.Campeonato;
import com.arthur.sportsclubscoringsystem.model.Clube;
import com.arthur.sportsclubscoringsystem.model.Participacao;
import com.arthur.sportsclubscoringsystem.repository.CampeonatoRepository;
import com.arthur.sportsclubscoringsystem.repository.ClubeRepository;
import com.arthur.sportsclubscoringsystem.repository.ParticipacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ParticipacaoService {

    private final ParticipacaoRepository participacaoRepository;
    private final CampeonatoRepository campeonatoRepository;
    private final ClubeRepository clubeRepository;

    public ParticipacaoService(ParticipacaoRepository participacaoRepository, CampeonatoRepository campeonatoRepository, ClubeRepository clubeRepository) {
        this.participacaoRepository = participacaoRepository;
        this.campeonatoRepository = campeonatoRepository;
        this.clubeRepository = clubeRepository;
    }

    public Double pontos(Long idCampeonato, Posicao posicao) {
        Campeonato campeonato = campeonatoRepository.buscarPorId(idCampeonato);
        return posicao.getPontosBase() * campeonato.getNivel().getMultiplicador();
    }

    public Double pontosCampService(Campeonato antigo, Posicao posicao) {
        return posicao.getPontosBase() * antigo.getNivel().getMultiplicador();
    }

    public void atualizarClubePtsHist(Long id, Double pontos, Participacao participacao) {
        ClubeService clubeService = new ClubeService(clubeRepository);
        clubeService.atualizarPontosEResultados(id, pontos, participacao);
    }
    public void atualizarCampeonatoRst(Long id, List<Participacao> resultados) {
        CampeonatoService campeonatoService = new CampeonatoService(campeonatoRepository, participacaoRepository, clubeRepository);
        campeonatoService.atualizarResultados(id, resultados);
    }
    public ParticipacaoResponseDTO salvar(ParticipacaoRequestDTO dto) {

        if (dto.getIdCampeonato() == null) {
            throw new IllegalArgumentException("ID Campeonato inválida.");
        }
        if (clubeRepository.buscarPorId(dto.getIdClube()).getDataFundacao().isAfter(campeonatoRepository.buscarPorId(dto.getIdCampeonato()).getDataInicio())) {
            throw new IllegalArgumentException("O clube não existia na data de ínicio do torneio.");
        }
        if (dto.getIdClube() == null) {
            throw new IllegalArgumentException("ID Clube inválida.");
        }
        if (dto.getPosicao() == null) {
            throw new IllegalArgumentException("Posição inválida.");
        }

        Campeonato campeonato = campeonatoRepository.buscarPorId(dto.getIdCampeonato());
        if (campeonato == null) {
            throw new IllegalArgumentException("ID Campeonato inválida.");
        }
        List<Participacao> resultados = campeonato.getResultados();
        for (int i = 0 ; i < resultados.toArray().length ; i++) {
            if(dto.getPosicao() == Posicao.PRIMEIRO || dto.getPosicao() == Posicao.SEGUNDO || dto.getPosicao() == Posicao.TERCEIRO) {
                if (resultados.get(i).getPosicao() == dto.getPosicao()) {
                    throw new IllegalArgumentException("Já existe um clube nesse lugar do pódio.");
                }
            }
        }

        Clube clube = clubeRepository.buscarPorId(dto.getIdClube());
        if (clube == null) {
            throw new IllegalArgumentException("ID Clube inválido.");
        }
        if (dto.getPosicao() == Posicao.PRIMEIRO || dto.getPosicao() == Posicao.SEGUNDO || dto.getPosicao() == Posicao.TERCEIRO) {
            if (participacaoRepository.buscarPorClubeECampeonato(dto.getIdClube(), dto.getIdCampeonato()) != null) {
                throw new IllegalArgumentException("Esse clube já foi registrado nesse campeonato.");
            }
        }

        Participacao participacao = new Participacao();

        participacao.setCampeonato(campeonato);
        participacao.setClube(clube);
        participacao.setPosicao(dto.getPosicao());
        resultados.add(participacao);
        participacaoRepository.salvar(participacao);
        atualizarClubePtsHist(clube.getId(), pontos(dto.getIdCampeonato(), dto.getPosicao()), participacao);
        atualizarCampeonatoRst(dto.getIdCampeonato(), resultados);

        return new ParticipacaoResponseDTO(
                participacao.getId(),
                participacao.getClube().getNome(),
                participacao.getCampeonato().getNome(),
                participacao.getPosicao()
        );
    }

    public ParticipacaoResponseDTO buscarPorId(Long id) {

        Participacao participacao = participacaoRepository.buscarPorId(id);

        if(participacao == null) {
            throw new IllegalArgumentException("ID inválido.");
        }

        return new ParticipacaoResponseDTO(
                participacao.getId(),
                participacao.getClube().getNome(),
                participacao.getCampeonato().getNome(),
                participacao.getPosicao()
        );
    }

    public List<ParticipacaoResponseDTO> buscarTodos(){

        return participacaoRepository.buscarTodos()
                .stream()
                .map(participacao -> new ParticipacaoResponseDTO(
                        participacao.getId(),
                        participacao.getClube().getNome(),
                        participacao.getCampeonato().getNome(),
                        participacao.getPosicao()
                )).toList();
    }

    public ParticipacaoResponseDTO atualizar(Long id, ParticipacaoRequestDTO dto) {
        Participacao participacaoExistente = participacaoRepository.buscarPorId(id);

        if (participacaoExistente == null) {
            throw new IllegalArgumentException("ID Inválido.");
        }

        Clube clubeAntigo = participacaoExistente.getClube();
        Campeonato campeonatoAntigo = participacaoExistente.getCampeonato();
        Posicao posicaoAntiga = participacaoExistente.getPosicao();

        if (dto.getIdCampeonato() == null) {
            throw new IllegalArgumentException("ID Campeonato inválida.");
        }
        if (dto.getIdClube() == null) {
            throw new IllegalArgumentException("ID Clube inválida.");
        }
        if(dto.getPosicao() == null) {
            throw new IllegalArgumentException("Posição inválida.");
        }

        Campeonato campeonato = campeonatoRepository.buscarPorId(dto.getIdCampeonato());
        if (campeonato == null) {
            throw new IllegalArgumentException("ID Campeonato inválida.");
        }
        List<Participacao> resultados = campeonato.getResultados();
        for (int i = 0 ; i < resultados.toArray().length ; i++) {
            if(dto.getPosicao() == Posicao.PRIMEIRO || dto.getPosicao() == Posicao.SEGUNDO || dto.getPosicao() == Posicao.TERCEIRO) {
                if (resultados.get(i).getPosicao() == dto.getPosicao() && !resultados.get(i).getId().equals(id)) {
                    throw new IllegalArgumentException("Já existe um clube nesse lugar do pódio.");
                }
            }
        }
        Clube clube = clubeRepository.buscarPorId(dto.getIdClube());
        if (clube == null) {
            throw new IllegalArgumentException("ID Clube inválido.");
        }

        clubeAntigo.setPontuacaoTotal(
                clubeAntigo.getPontuacaoTotal()
                        - pontos(campeonatoAntigo.getId(), posicaoAntiga)
        );
        List<Participacao> participacoes = clubeAntigo.getParticipacoes();
        participacoes.removeIf(
                p -> p.getId().equals(participacaoExistente.getId())
        );
        clubeAntigo.setParticipacoes(participacoes);
        clubeRepository.atualizar(clubeAntigo);

        participacaoExistente.setPosicao(dto.getPosicao());
        participacaoExistente.setCampeonato(campeonatoRepository.buscarPorId(dto.getIdCampeonato()));
        participacaoExistente.setClube(clubeRepository.buscarPorId(dto.getIdClube()));

        resultados.add(participacaoExistente);
        atualizarCampeonatoRst(dto.getIdCampeonato(), resultados);
        atualizarClubePtsHist(dto.getIdClube(), pontos(dto.getIdCampeonato(), dto.getPosicao()), participacaoExistente);

        return new ParticipacaoResponseDTO(
                participacaoExistente.getId(),
                participacaoExistente.getClube().getNome(),
                participacaoExistente.getCampeonato().getNome(),
                participacaoExistente.getPosicao()
        );
    }

    public ParticipacaoResponseDTO atualizarCampService(Long id, ParticipacaoRequestDTO dto, Campeonato antigo) {
        Participacao participacaoExistente = participacaoRepository.buscarPorId(id);

        if (participacaoExistente == null) {
            throw new IllegalArgumentException("ID Inválido.");
        }

        if (dto.getIdCampeonato() == null) {
            throw new IllegalArgumentException("ID Campeonato inválida.");
        }
        if (dto.getIdClube() == null) {
            throw new IllegalArgumentException("ID Clube inválida.");
        }
        if(dto.getPosicao() == null) {
            throw new IllegalArgumentException("Posição inválida.");
        }

        Campeonato campeonato = antigo;
        if (campeonato == null) {
            throw new IllegalArgumentException("ID Campeonato inválida.");
        }

        Clube clube = clubeRepository.buscarPorId(dto.getIdClube());
        if (clube == null) {
            throw new IllegalArgumentException("ID Clube inválido.");
        }

        clube.setPontuacaoTotal(clube.getPontuacaoTotal() - pontosCampService(
                antigo,
                participacaoExistente.getPosicao()
        ));
        List<Participacao> participacoes = clube.getParticipacoes();
        participacoes.remove(participacaoExistente);
        clube.setParticipacoes(participacoes);
        clubeRepository.atualizar(clube);

        participacaoExistente.setPosicao(dto.getPosicao());
        participacaoExistente.setCampeonato(campeonatoRepository.buscarPorId(dto.getIdCampeonato()));
        participacaoExistente.setClube(clubeRepository.buscarPorId(dto.getIdClube()));
        participacaoRepository.atualizar(participacaoExistente);
        atualizarClubePtsHist(dto.getIdClube(), pontos(dto.getIdCampeonato(), dto.getPosicao()), participacaoExistente);

        return new ParticipacaoResponseDTO(
                participacaoExistente.getId(),
                participacaoExistente.getClube().getNome(),
                participacaoExistente.getCampeonato().getNome(),
                participacaoExistente.getPosicao()
        );
    }

    public void excluir(Long id) {

        Participacao participacaoExistente = participacaoRepository.buscarPorId(id);

        if (participacaoExistente == null) {
            throw new IllegalArgumentException("ID inválido.");
        }

        Campeonato campeonato = campeonatoRepository.buscarPorId(participacaoExistente.getCampeonato().getId());
        if (campeonato == null) {
            throw new IllegalArgumentException("ID Campeonato inválida.");
        }
        Clube clube = clubeRepository.buscarPorId(participacaoExistente.getClube().getId());
        if (clube == null) {
            throw new IllegalArgumentException("ID Clube inválido.");
        }

        clube.setPontuacaoTotal(clube.getPontuacaoTotal() - pontos(participacaoExistente
                .getCampeonato()
                .getId(), participacaoExistente.getPosicao()));
        List<Participacao> participacoes = clube.getParticipacoes();
        participacoes.remove(participacaoExistente);
        clube.setParticipacoes(participacoes);

        List<Participacao> resultados = campeonato.getResultados();
        resultados.remove(participacaoExistente);
        campeonato.setResultados(resultados);

        clubeRepository.atualizar(clube);
        campeonatoRepository.atualizar(campeonato);

        participacaoRepository.excluir(id);
    }

}