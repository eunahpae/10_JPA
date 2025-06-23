package com.ohgiraffers.jpql.section01.simple;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SimpleJPQLRepositoryTests {

    @Autowired
    private SimpleJPQLRepository simpleJPQLRepository;

    @DisplayName("TypedQuery를 이용한 단일행, 단일컬럼 조회 테스트")
    @Test
    void testSelectSingleMenuByTypedQuery() {

        // given
        // when
        String menuName =  simpleJPQLRepository.selectSingleMenuByTypedQuery();

        // then
        assertEquals(menuName, "한우딸기국밥");

    }

    @DisplayName("TypedQuery를 이용한 다중행 조회 테스트")
    @Test
    void testSelectMultiMenuByTypedQuery() {

        // given
        // when
        List<Menu> menuList =  simpleJPQLRepository.selectMultiMenuByTypedQuery();

        // then
        assertNotNull(menuList);
        System.out.println("menuList = " + menuList);

    }

    @DisplayName("DISTINCT 연산자 사용 조회 테스트")
    @Test
    void testSelectUsingDistinct() {

        // given
        // when
        List<Integer> categoryList = simpleJPQLRepository.selectUsingDistinct();

        // then
        assertNotNull(categoryList);
        System.out.println("categoryList = " + categoryList);

    }

    @DisplayName("IN 연산자 사용 조회 테스트")
    @Test
    void testSelectUsingIn() {

        // given
        // when
        List<Menu> menuList = simpleJPQLRepository.selectUsingIn();

        // then
        assertNotNull(menuList);
        System.out.println("menuList = " + menuList);
    }

    @DisplayName("LIKE 연산자 사용 조회 테스트")
    @Test
    void testSelectUsingLike() {

        // given
        // when
        List<Menu> menuList = simpleJPQLRepository.selectUsingLike();

        // then
        assertNotNull(menuList);
        System.out.println("menuList = " + menuList);
    }

}
