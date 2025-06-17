package com.ohgiraffers.section02.crud;

import jakarta.persistence.EntityManager;

public class EntityManagerCRUD {

    private EntityManager entityManager;

    /* 1. 특정 메뉴 코드로 메뉴 조회하는 기능 */
    public Menu findMenuByMenuCode(int menuCode) {
        entityManager = EntityManagerGenerator.getInstance();
        return entityManager.find(Menu.class, menuCode);
    }

}
