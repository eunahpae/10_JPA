package com.ohgiraffers.springdatajpa.menu.repository;

import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MenuRepository 인터페이스는 Spring Data JPA의 JpaRepository를 상속받아
 * Menu 엔티티에 대한 기본적인 CRUD 및 페이징, 정렬 기능을 자동으로 제공한다.
 *
 * JpaRepository<Menu, Integer> 상속으로 인해 아래와 같은 메서드들이 자동으로 사용 가능하다:
 * - 저장: save(), saveAll()
 * - 조회: findById(), findAll(), findAllById()
 * - 삭제: deleteById(), delete(), deleteAll()
 * - 기타: count(), existsById() 등
 *
 * 별도의 구현 클래스 없이도 Spring Data JPA가 런타임 시 구현체를 생성해 스프링 빈으로 등록하므로, 바로 주입하여 사용할 수 있다.
 *
 * @Repository 어노테이션은 생략 가능하지만, 명시적으로 붙이면 예외 변환과 계층 구조 명확화에 도움이 된다.
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    // 기본 CRUD 기능은 JpaRepository가 제공하므로 별도 메서드 선언 필요 없음.

    /* ✅ 사용자 정의 쿼리 메서드 정의 영역 */

    /**
     * 전달받은 가격보다 높은 가격을 가진 메뉴 목록 조회
     *
     * @param menuPrice 기준 가격
     * @return menuPrice 초과 메뉴 목록
     */
    List<Menu> findByMenuPriceGreaterThan(Integer menuPrice);

    /**
     * 전달받은 가격보다 높은 가격의 메뉴 목록을 menuPrice 기준 오름차순 정렬하여 조회
     *
     * @param menuPrice 기준 가격
     * @return 정렬된 메뉴 목록
     */
    List<Menu> findByMenuPriceGreaterThanOrderByMenuPrice(Integer menuPrice);

    /**
     * 메서드명이 복잡해지는 것을 방지하고, 외부에서 동적으로 정렬 기준을 전달할 수 있도록 정렬 조건을 파라미터로 분리
     *
     * 예: Sort.by("menuName").descending()
     *
     * @param menuPrice 기준 가격
     * @param sort 정렬 조건
     * @return 정렬된 메뉴 목록
     */
    List<Menu> findByMenuPriceGreaterThan(Integer menuPrice, Sort sort);
}