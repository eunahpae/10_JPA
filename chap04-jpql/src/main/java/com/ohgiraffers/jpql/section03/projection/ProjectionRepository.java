package com.ohgiraffers.jpql.section03.projection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Menu> singleEntityProjection() {
        String jpql = "SELECT m FROM Section03Menu m";
        return entityManager.createQuery(jpql, Menu.class).getResultList();
    }


}
