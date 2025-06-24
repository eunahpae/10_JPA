package com.ohgiraffers.springdatajpa.menu.repository;

import com.ohgiraffers.springdatajpa.menu.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    /*
     * [1] JPQL 사용 (비활성화된 코드)
     *
     * - Entity 객체를 대상으로 하는 객체 지향 쿼리
     * - SELECT 대상은 엔티티 자체 또는 엔티티의 속성
     * - 예시: "SELECT c FROM Category c" → 전체 Category 엔티티를 조회
     * - JPQL에서는 엔티티 필드명을 기준으로 쿼리 작성하며, 테이블명/컬럼명이 아님
     */
    // @Query(value = "SELECT c FROM Category c ORDER BY c.categoryCode")
    /**
     * [2] Native SQL 사용
     *
     * - 실제 DB 테이블과 컬럼명을 기반으로 작성된 SQL 쿼리
     * - nativeQuery = true 옵션 필수
     * - 주로 복잡한 SQL, JOIN, 서브쿼리, 성능 최적화된 쿼리 등에서 사용
     * - 조회 대상 컬럼명을 직접 명시해야 하며, 결과는 Entity에 매핑되어야 함
     *
     * @return DB의 tbl_category 테이블에서 category_code 기준 오름차순으로 정렬된 전체 카테고리 목록
     */
    @Query(
        value = "SELECT category_code, category_name, ref_category_code FROM tbl_category ORDER BY category_code", nativeQuery = true)
    List<Category> findAllCategory();
}
