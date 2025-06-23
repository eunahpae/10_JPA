package com.ohgiraffers.nativequery.section02.namedquery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class NamedQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> selectByNamedNativeQuery() {
        Query nativeQuery = entityManager.createNamedQuery("Category.menuCountOfCategory");
        return nativeQuery.getResultList();
    }

}
