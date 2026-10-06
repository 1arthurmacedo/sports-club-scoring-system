package com.arthur.sportsclubscoringsystem.repository;

import com.arthur.sportsclubscoringsystem.model.Campeonato;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CampeonatoRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void salvar(Campeonato campeonato) { entityManager.persist(campeonato); }

    public Campeonato buscarPorId(Long id) {
        return entityManager.find(Campeonato.class, id);
    }

    public List<Campeonato> buscarTodos() {
        return entityManager
                .createQuery("SELECT c FROM Campeonato c", Campeonato.class)
                .getResultList();
    }

    @Transactional
    public void atualizar(Campeonato campeonato){ entityManager.merge(campeonato); }

    @Transactional
    public void excluir(Long id) {
        Campeonato campeonato = buscarPorId(id);
        if (campeonato != null) {
            entityManager.remove(campeonato);
        }
    }
}