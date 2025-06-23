package com.ohgiraffers.jpql.section05.groupfunction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class GroupFunctionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 특정 카테고리에 속한 메뉴의 개수를 반환한다.
     *
     * COUNT는 JPQL의 대표적인 그룹 함수이며,
     * 조건에 맞는 엔티티가 하나도 없을 경우에도 0을 반환한다.
     * 따라서 기본형(long)으로 안전하게 받을 수 있다.
     *
     * @param categoryCode 카테고리 코드
     * @return 해당 카테고리에 속한 메뉴 개수
     */
    public long countMenuOfCategory(int categoryCode) {
        String jpql
            = "SELECT COUNT(m.menuCode) FROM Section05Menu m "
            + "WHERE m.categoryCode = :categoryCode";
        long countOfMenu = entityManager.createQuery(jpql, Long.class)
            .setParameter("categoryCode", categoryCode)
            .getSingleResult();
        return countOfMenu;
    }

    /**
     * 특정 카테고리에 속한 메뉴들의 총 가격 합계를 반환한다.
     *
     * SUM은 조건에 맞는 레코드가 없을 경우 null을 반환하므로,
     * 기본형(long 등)이 아닌 Long(Wrapper)을 사용해야 NPE를 방지할 수 있다.
     *
     * 필요 시 호출하는 측에서 null 체크 또는 디폴트 처리(예: 0L)를 해야 한다.
     *
     * @param categoryCode 카테고리 코드
     * @return 해당 카테고리의 총 가격 합 (없으면 null)
     */
    public Long otherWithNoResult(int categoryCode) {
        String jpql = "SELECT SUM(m.menuPrice) FROM Section05Menu m WHERE m.categoryCode = :categoryCode";

        /*
         * JPQL에서 COUNT를 제외한 그룹 함수(SUM, AVG, MAX, MIN)는
         * 조건에 맞는 레코드가 없을 경우 null을 반환한다.
         * 이때 기본형(long, double 등)에 바로 할당하면 언박싱 과정에서
         * NullPointerException이 발생하므로, Wrapper 타입을 사용해야 한다.
         */
        Long sumOfMenu = entityManager.createQuery(jpql, Long.class)
            .setParameter("categoryCode", categoryCode).getSingleResult();
        return sumOfMenu;
    }

    /**
     * 카테고리별로 메뉴 가격의 합계를 구한 후, 주어진 금액 이상인 카테고리만 반환한다.
     *
     * GROUP BY를 사용하여 categoryCode별로 집계하고,
     * HAVING 절로 그룹 조건(minPrice 이상)을 필터링한다.
     *
     * 반환 타입은 Object[]이며, 각 행은 [categoryCode, SUM(menuPrice)] 순서로 구성된다.
     *
     * @param minPrice 그룹별 합계 최소 금액
     * @return 조건을 만족하는 [카테고리 코드, 메뉴 가격 합계] 리스트
     */
    public List<Object[]> selectByGroupByHaving(long minPrice) {
        String jpql = "SELECT m.categoryCode, SUM(m.menuPrice) " +
            "FROM Section05Menu m " +
            "GROUP BY m.categoryCode " +
            "HAVING SUM(m.menuPrice) >= :minPrice";

        List<Object[]> sumPriceOfCategoryList = entityManager.createQuery(jpql)
            .setParameter("minPrice", minPrice).getResultList();
        return sumPriceOfCategoryList;
    }
}
