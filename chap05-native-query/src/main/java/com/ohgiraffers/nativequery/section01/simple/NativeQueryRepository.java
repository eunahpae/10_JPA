package com.ohgiraffers.nativequery.section01.simple;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class NativeQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 네이티브 SQL을 사용하여 메뉴 정보를 조회한다.
     * <p>
     * - createNativeQuery(String sql, Class resultClass)를 사용하면 SQL 쿼리 결과를 지정한 JPA 엔티티(Menu.class)에
     * 직접 매핑할 수 있다.
     * <p>
     * - 이때 엔티티(Menu)는 @Entity로 선언되어 있어야 하며, SQL에서 조회하는 컬럼 명칭이 엔티티 필드명 또는 @Column(name = "...") 값과
     * 일치해야 한다.
     * <p>
     * - 또한 엔티티에 매핑하려면 조회 쿼리에 해당 엔티티의 모든 필드가 포함되어야 하며, 일부 컬럼만 조회할 경우 매핑에 실패하거나 예외가 발생할 수 있다.
     * <p>
     * - 쿼리 파라미터는 위치 기반(?) 방식으로 설정하며, 인덱스는 1부터 시작한다.
     * <p>
     * 장점: - 복잡한 SQL, DB 고유 기능(예: 윈도우 함수, 서브쿼리, 뷰 등)을 그대로 사용할 수 있다. - 엔티티 매핑을 통해 객체지향 방식으로 결과를 다룰 수
     * 있다.
     * <p>
     * 주의: - 매핑 실패 시 ClassCastException 또는 MappingException 발생 가능 - SQL이 문자열로 하드코딩되므로 유지보수와 보안(SQL
     * Injection) 주의 필요
     *
     * @param menuCode 메뉴 코드 (PK)
     * @return 해당 메뉴 정보를 담은 Menu 엔티티
     */
    public Menu nativeQueryByResultType(int menuCode) {

        // Native Query로 결과를 엔티티에 매핑하려면 **모든 컬럼**을 조회해야 한다.
        // 일부 컬럼만 조회하면 JPA가 매핑할 수 없어 오류가 발생할 수 있다.
        String query = "SELECT menu_code, menu_name, menu_price, category_code, orderable_status "
            + "FROM tbl_menu WHERE menu_code = ?";

        Query nativeQuery = entityManager.createNativeQuery(query, Menu.class)
            // setParameter(1, ...)은 위치 기반 파라미터 바인딩이다.
            // 네이티브 쿼리에서 물음표(?)는 1번부터 시작하며,
            // 이 경우 첫 번째 ? 자리에 menuCode 값이 바인딩된다.
            .setParameter(1, menuCode);

        return (Menu) nativeQuery.getSingleResult();
    }

    /**
     * 엔티티 매핑 없이 네이티브 SQL을 사용하여 일부 컬럼만 조회한다.
     * <p>
     * - 엔티티가 아닌 순수 SQL 컬럼 결과를 조회할 때 사용하는 방식이다. - 엔티티 클래스 지정 없이 createNativeQuery(String sql)만 사용하며,
     * 반환 결과는 Object[] 배열 또는 Object(단일 컬럼일 경우)로 구성된다.
     * <p>
     * 예: SELECT menu_name, menu_price ... → 결과는 List<Object[]>이며, 각 Object[]는 [menu_name,
     * menu_price] 형태로 반환된다.
     * <p>
     * 장점: - 필요한 컬럼만 조회 가능하므로 성능 최적화에 유리하다. - DTO로 직접 매핑하거나 커스터마이징이 쉬움
     * <p>
     * 주의: - 직접 타입 캐스팅이 필요하므로 코드에서 구조를 알고 있어야 한다. - 반환 값을 엔티티처럼 바로 다룰 수 없으며, 별도로 DTO 변환 또는 후처리 필요
     *
     * @return 메뉴 이름과 가격 정보 리스트 (Object[] 형태)
     */
    public List<Object[]> nativeQueryByNoResultType() {

        String query = "SELECT menu_name, menu_price FROM tbl_menu";

        Query nativeQuery = entityManager.createNativeQuery(query);

        return nativeQuery.getResultList();
    }

    public List<Object[]> nativeQueryByAutoMapping() {
        /*
         * 카테고리별 등록된 메뉴 개수를 조회하는 네이티브 SQL 쿼리이다.
         *
         * 주요 동작:
         * - tbl_category 테이블을 기준으로 LEFT JOIN을 수행한다.
         * - 하위 쿼리(서브쿼리)는 tbl_menu 테이블에서 카테고리별 메뉴 수를 COUNT하여
         *   category_code 기준으로 그룹화된 임시 테이블을 생성한다.
         *
         * 서브쿼리 설명:
         *   SELECT COUNT(*) AS menu_count, b.category_code
         *   FROM tbl_menu b
         *   GROUP BY b.category_code
         *
         *   → 각 카테고리별 등록된 메뉴 수를 계산
         *
         * 메인 쿼리 설명:
         *   SELECT a.category_code, a.category_name, a.ref_category_code,
         *          COALESCE(v.menu_count, 0) AS menu_count
         *   FROM tbl_category a
         *   LEFT JOIN (...) v ON (a.category_code = v.category_code)
         *   ORDER BY 1
         *
         *   → 모든 카테고리를 기준으로 메뉴 수를 LEFT JOIN하여 함께 조회
         *   → 메뉴가 없는 카테고리의 경우 v.menu_count가 NULL이 되므로,
         *     COALESCE 함수를 사용하여 0으로 치환한다.
         *
         * 사용된 JPA 기능:
         * - createNativeQuery(query, "categoryCountAutoMapping") 를 통해
         *   SQL 결과를 @SqlResultSetMapping 이름을 통해 자동 매핑한다.
         * - "categoryCountAutoMapping"은 미리 정의된 ResultSet 매핑 정보로,
         *   SQL의 결과 컬럼을 특정 DTO 또는 매핑 클래스로 매핑할 수 있게 한다.
         *
         * 반환 형식:
         * - 결과는 Object[] 배열 리스트이며,
         *   각 Object[]는 [category_code, category_name, ref_category_code, menu_count] 순서를 가진다.
         */
        String query
            = "SELECT a.category_code, a.category_name, a.ref_category_code," +
            " COALESCE(v.menu_count, 0) menu_count FROM tbl_category a" +
            " LEFT JOIN (SELECT COUNT(*) AS menu_count, b.category_code FROM tbl_menu b" +
            " GROUP BY b.category_code) v ON (a.category_code = v.category_code)" +
            " ORDER BY 1";
        Query nativeQuery
            = entityManager.createNativeQuery(query, "categoryCountAutoMapping");
        return nativeQuery.getResultList();
    }

    public List<Object[]> nativeQueryByManualMapping() {
        String query
            = "SELECT a.category_code, a.category_name, a.ref_category_code," +
            " COALESCE(v.menu_count, 0) menu_count" +
            " FROM tbl_category a" +
            " LEFT JOIN (SELECT COUNT(*) AS menu_count, b.category_code" +
            " FROM tbl_menu b" +
            " GROUP BY b.category_code) v ON (a.category_code = v.category_code)" +
            " ORDER BY 1";
        Query nativeQuery
            = entityManager.createNativeQuery(query, "categoryCountManualMapping");
        return nativeQuery.getResultList();
    }

}
