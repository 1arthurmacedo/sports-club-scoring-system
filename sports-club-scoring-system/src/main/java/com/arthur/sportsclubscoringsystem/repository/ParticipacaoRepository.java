package com.arthur.sportsclubscoringsystem.repository;

import com.arthur.sportsclubscoringsystem.enums.Posicao;
import com.arthur.sportsclubscoringsystem.model.Participacao;
import com.arthur.sportsclubscoringsystem.model.Clube;
import com.arthur.sportsclubscoringsystem.model.Campeonato;
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
                .createQuery("SELECT p FROM participacoes p", Participacao.class)
                .getResultList();
    }

    public Participacao buscarPorClubeECampeonato(Long clubeId, Long campeonatoId) {
        try{
            return entityManager
                    .createQuery(
                            "SELECT p FROM participacoes p WHERE c.clube_id = :clubeId AND c.campeonato_id = :campeonatoId",
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
        try{
            return entityManager
                    .createQuery(
                            "SELECT p FROM participacoes p WHERE p.campeonato_id = :campeonatoId AND p.posicao = :posicao",
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
    public void atualizar(Participacao participacao){ entityManager.merge(participacao);}

    @Transactional
    public void excluir(Long id) {
        Participacao participacao = buscarPorId(id);

        if (participacao != null) {
            entityManager.remove(participacao);
        }
    }
}
