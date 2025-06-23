package com.ohgiraffers.jpql.section06.join;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class JoinRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 내부 조인(INNER JOIN)을 통해 Menu와 Category 엔티티를 조인하여 Menu 엔티티만 조회
     *
     * 주의:
     * - JPQL에서 JOIN만 사용할 경우, 연관 엔티티(m.category)는 지연 로딩(Lazy Loading)이므로
     *   해당 연관 필드에 접근하는 시점에 추가 쿼리가 발생할 수 있다.
     * - 이로 인해 N개의 Menu를 조회할 때 N개의 Category를 조회하는 추가 쿼리(N+1 문제)가 발생할 수 있다.
     *
     * 해결 방법:
     * - fetch join을 사용하여 연관 엔티티를 한 번의 쿼리로 함께 조회할 수 있다.
     *
     * @return Menu 목록 (Category는 아직 로딩되지 않았을 수 있음)
     */
    public List<Menu> selectByInnerJoin() {
        String jpql = "SELECT m FROM Section06Menu m JOIN m.category c";
        return entityManager.createQuery(jpql, Menu.class).getResultList();
    }

    /**
     * fetch join을 사용하여 Menu와 Category 함께 조회
     *
     * - fetch join은 연관된 엔티티를 즉시 로딩(Eager loading)하도록 JPQL에 명시하는 방법이다.
     * - 내부 조인(INNER JOIN) 방식이며, Menu와 그에 연결된 Category를 한 번의 쿼리로 모두 조회한다.
     * - N+1 문제를 방지하고 성능을 개선할 수 있다.
     *
     * @return Category가 함께 로딩된 Menu 목록
     */
    public List<Menu> selectByFetchJoin() {
        String jpql = "SELECT m FROM Section06Menu m JOIN FETCH m.category c";
        return entityManager.createQuery(jpql, Menu.class).getResultList();
    }

    /**
     * 외부 조인(RIGHT JOIN)을 사용하여 카테고리를 기준으로 메뉴 정보 조회
     *
     * - 오른쪽 엔티티(Category)가 기준이 되며, 카테고리는 존재하지만 연결된 메뉴가 없는 경우에도 결과에 포함된다.
     * - 반환 타입은 Object[]이며, 각 배열 요소는 메뉴명과 카테고리명 순서이다.
     * - 메뉴가 없는 카테고리는 메뉴명이 null로 반환된다.
     *
     * @return [menuName, categoryName] 형태의 Object[] 목록
     */
    public List<Object[]> selectByOuterJoin() {
        String jpql = "SELECT m.menuName, c.categoryName FROM Section06Menu m "
            + "RIGHT JOIN m.category c ORDER BY m.category.categoryCode";
        return entityManager.createQuery(jpql).getResultList();
    }

    public List<Object[]> selectByCollectionJoin() {
        String jpql = "SELECT m.menuName, c.categoryName FROM Section06Category c "
            + "RIGHT JOIN c.menuList m ORDER BY m.category.categoryCode";
        return entityManager.createQuery(jpql).getResultList();
    }
}
