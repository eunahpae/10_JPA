package com.ohgiraffers.jpql.section04.paging;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class PagingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Menu> usingPagingAPI(int offset, int limit) {
        String jpql = "SELECT m FROM Section04Menu m ORDER BY m.menuCode DESC";

        List<Menu> pagingMenuList = entityManager.createQuery(jpql, Menu.class)
            .setFirstResult(offset)   // 조회 시작 위치 (0부터)
            .setMaxResults(limit)    // 조회 데이터 최대 개수
            .getResultList();

        return pagingMenuList;

    }

}
