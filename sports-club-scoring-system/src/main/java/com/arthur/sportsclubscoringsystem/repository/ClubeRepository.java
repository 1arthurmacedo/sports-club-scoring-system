package com.arthur.sportsclubscoringsystem.repository;

import com.arthur.sportsclubscoringsystem.model.Clube;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ClubeRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void salvar(Clube clube) { entityManager.persist(clube); }

    public Clube buscarPorId(Long id) {
        return entityManager.find(Clube.class, id);
    }

    public List<Clube> buscarTodos() {
        return entityManager
                .createQuery("SELECT c FROM Clube c", Clube.class) // Corrigido: 'Clube' em vez de 'clubes'
                .getResultList();
    }

    public Clube buscarPorNome(String nome) {
        try {
            return entityManager
                    .createQuery(
                            "SELECT c FROM Clube c WHERE c.nome = :nome", // Corrigido: 'Clube' em vez de 'clube'
                            Clube.class
                    )
                    .setParameter("nome", nome)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public void atualizar(Clube clube){ entityManager.merge(clube); }

    @Transactional
    public void excluir(Long id) {
        Clube clube = buscarPorId(id);
        if (clube != null) {
            entityManager.remove(clube);
        }
    }
}