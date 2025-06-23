package com.ohgiraffers.springdatajpa.menu.repository;

import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * JpaRepository<엔티티, Id타입> 를 상속받으면 Spring Data JPA가 자동으로 구현체를 생성하고,
 * 이를 Spring 빈으로 등록해 주기 때문에 @Repository 어노테이션은 생략해도 동작한다.
 *
 * 다만, @Repository를 명시적으로 붙이면 다음과 같은 장점이 있다:
 * - 해당 클래스가 명확히 'Repository 계층'임을 보여줘 가독성과 구조 이해에 도움
 * - Spring의 AOP를 통한 예외 변환(DataAccessException 변환)이 적용될 수 있다
 * - @Controller, @Service와 함께 계층 구분의 일관성을 유지할 수 있다
 *
 * 따라서 기능적으로는 생략 가능하지만, 관례적으로 붙이는 것이 좋다.
 */
@Repository  // 생략 가능하지만 명시적으로 붙이면 계층 구조 명확해짐
public interface MenuRepository extends JpaRepository<Menu, Integer> {



}
