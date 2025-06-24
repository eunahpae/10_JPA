package com.ohgiraffers.springdatajpa.menu.repository;

import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MenuRepository 인터페이스는 Spring Data JPA의 JpaRepository를 상속받아
 * Menu 엔티티에 대한 기본적인 CRUD 및 페이징, 정렬 기능을 자동으로 제공한다.
 *
 * JpaRepository<Menu, Integer> 상속으로 인해 아래와 같은 메서드들이 자동으로 사용 가능하다:
 * - save(), saveAll()
 * - findById(), findAll(), findAllById()
 * - deleteById(), delete(), deleteAll()
 * - count()
 * - existsById()
 *
 * 별도의 구현 클래스 없이도 Spring Data JPA가 런타임 시 구현체를 생성해
 * 스프링 빈으로 등록하므로, 바로 주입하여 사용할 수 있다.
 *
 * @Repository 어노테이션은 생략 가능하지만,
 * 명시적으로 붙이면 예외 변환과 계층 구조 명확화에 도움이 된다.
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    // 기본 CRUD 기능은 JpaRepository가 제공하므로 별도 메서드 선언 필요 없음.

}
