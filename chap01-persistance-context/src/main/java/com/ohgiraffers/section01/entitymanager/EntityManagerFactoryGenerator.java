package com.ohgiraffers.section01.entitymanager;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactoryGenerator {

    private static final EntityManagerFactory factory
        = Persistence.createEntityManagerFactory("jpatest");

    private EntityManagerFactoryGenerator() {
    }

    public static EntityManagerFactory getInstance() {
        return factory;
    }

}
