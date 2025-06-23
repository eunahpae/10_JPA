package com.ohgiraffers.jpql.section06.join;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JoinRepositoryTests {

    @Autowired
    private JoinRepository joinRepository;

    @DisplayName("InnerJoin을 이용한 조회 테스트")
    @Test
    void testSelectByInnerJoin() {
        // given
        // when
        List<Menu> menuList = joinRepository.selectByInnerJoin();

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }

    @DisplayName("InnerJoin의 N+1문제를 해결할 수 있는 FetchJoin을 이용한 조회 테스트")
    @Test
    public void testSelectByFetchJoin() {
        //given
        //when
        List<Menu> menuList = joinRepository.selectByFetchJoin();
        //then
        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }

    @DisplayName("OuterJoin을 이용한 조회 테스트")
    @Test
    void testSelectByOuterJoin() {
        // given
        // when
        List<Object[]> menuList = joinRepository.selectByOuterJoin();

        // then
        assertNotNull(menuList);
        menuList.forEach(
            row -> {
                for (Object column : row) {
                    System.out.print(column + " ");
                }
                System.out.println();
            }
        );
    }

}
