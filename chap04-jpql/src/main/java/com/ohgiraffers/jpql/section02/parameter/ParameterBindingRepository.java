package com.ohgiraffers.jpql.section02.parameter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ParameterBindingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 1. 이름 기반 파라미터 바인딩을 사용하는 JPQL 쿼리
     *
     * - ":menuName"이라는 이름 기반 바인딩 파라미터를 사용하여 메뉴 이름으로 조회
     * - setParameter("menuName", menuName) 호출로 실제 값 바인딩
     */
    public List<Menu> selectMenuByBindingName(String menuName) {
        String jpql = "SELECT m FROM Section02Menu m WHERE menuName = :menuName";
        List<Menu> resultMenuList = entityManager.createQuery(jpql, Menu.class)
            .setParameter("menuName", menuName)  // 이름 기반 바인딩
            .getResultList();
        return resultMenuList;
    }

    /**
     * 2. 위치 기반 파라미터 바인딩을 사용하는 JPQL 쿼리
     *
     * - "?1"과 같이 위치 인덱스를 기준으로 파라미터 바인딩
     * - setParameter(1, menuName)을 통해 첫 번째 위치에 값 바인딩
     */
    public List<Menu> selectMenuByBindingPositiion(String menuName) {
        String jpql = "SELECT m FROM Section02Menu m WHERE menuName = ?1";
        return entityManager.createQuery(jpql, Menu.class)
            .setParameter(1, menuName)  // 위치 기반 바인딩
            .getResultList();
    }

}
