package com.ohgiraffers.jpql.section03.projection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProjectionRepositoryTests {

    @Autowired
    private ProjectionService projectionService;

    @DisplayName("")
    @Test
    void testSingleEntityProjection() {

        // given
        // when
        List<Menu> menuList = projectionService.singleEntityProjection();

        // then
        assertNotNull(menuList);
        System.out.println("menuList = " + menuList);
    }

}
