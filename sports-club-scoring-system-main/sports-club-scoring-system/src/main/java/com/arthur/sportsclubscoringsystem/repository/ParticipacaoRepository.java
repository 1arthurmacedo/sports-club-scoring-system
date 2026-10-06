package com.arthur.sportsclubscoringsystem.repository;

import com.arthur.sportsclubscoringsystem.enums.Posicao;
import com.arthur.sportsclubscoringsystem.model.Participacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ParticipacaoRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void salvar(Participacao participacao) { entityManager.persist(participacao); }

    public Participacao buscarPorId(Long id) {
        return entityManager.find(Participacao.class, id);
    }

    public List<Participacao> buscarTodos() {
        return entityManager
                .createQuery("SELECT p FROM Participacao p", Participacao.class) // Corrigido: 'Participacao' em vez de 'participacoes'
                .getResultList();
    }

    public Participacao buscarPorClubeECampeonato(Long clubeId, Long campeonatoId) {
        try {
            return entityManager
                    .createQuery(
                            "SELECT p FROM Participacao p WHERE p.clube.id = :clubeId AND p.campeonato.id = :campeonatoId", // Corrigido: 'Participacao' e relacionamentos Java 'p.clube.id' / 'p.campeonato.id'
                            Participacao.class
                    )
                    .setParameter("clubeId", clubeId)
                    .setParameter("campeonatoId", campeonatoId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Participacao buscarPorCampeonatoEPosicao(Long campeonatoId, Posicao posicao) {
        try {
            return entityManager
                    .createQuery(
                            "SELECT p FROM Participacao p WHERE p.campeonato.id = :campeonatoId AND p.posicao = :posicao", // Corrigido: 'Participacao' e 'p.campeonato.id'
                            Participacao.class
                    )
                    .setParameter("posicao", posicao)
                    .setParameter("campeonatoId", campeonatoId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public void atualizar(Participacao participacao){ entityManager.merge(participacao); }

    @Transactional
    public void excluir(Long id) {
        Participacao participacao = buscarPorId(id);
        if (participacao != null) {
            entityManager.remove(participacao);
        }
    }
}