package com.ohgiraffers.jpql.section04.paging;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PagingRepositoryTests {

    @Autowired
    private PagingRepository pagingRepository;

    @DisplayName("페이징 api를 이용한 조회 테스트")
    @Test
    void testUsingPagingAPI() {

        // given
        int offset = 10;
        int limit = 5;

        // when
        List<Menu> menuList = pagingRepository.usingPagingAPI(offset, limit);

        // then
        assertTrue(menuList.size() > 0 && menuList.size() < 6);
        menuList.forEach(System.out::println);

    }

}
