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
    @Autowired
    private ProjectionRepository projectionRepository;


    @DisplayName("단일 엔티티 프로젝션 테스트")
    @Test
    void testSingleEntityProjection() {

        // given
        // when
        List<Menu> menuList = projectionService.singleEntityProjection();

        // then
        assertNotNull(menuList);
        System.out.println("menuList = " + menuList);
    }

    @DisplayName("스칼라 타입 프로젝션 테스트")
    @Test
    void testScalarTypeProjection() {

        // given
        // when
        List<Object[]> categoryList = projectionRepository.scalarTypeProjection();

        // then
        assertNotNull(categoryList);

        categoryList.forEach(
            row -> {
                for (Object column : row) {
                    System.out.print(column + " ");
                }
                System.out.println();
            }
        );
    }

    @DisplayName("스칼라 타입 프로젝션 테스트")
    @Test
    void testNewCommandProjection() {

        // given
        // when
        List<CategoryInfo> categoryInfoList = projectionRepository.newCommandProjection();

        // then
        assertNotNull(categoryInfoList);

        categoryInfoList.forEach(System.out::println);
    }

}
