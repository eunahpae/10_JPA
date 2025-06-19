package com.ohgiraffers.mapping.section02.embedded;

import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository {

    // 스프링이 EntityManager를 주입하도록 해주는 어노테이션
    @PersistenceContext
    private EntityManager entityManager;

    public void save(Book book) {
        entityManager.persist(book);
    }

}
