package com.ohgiraffers.associationmapping.section03.bidirection;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BiDirectionTests {

    @Autowired
    private BiDirectionService biDirectionService;


    @DisplayName("양방향 연관 관계 매핑 조회(연관관계의 주인)")
    @Test
    void biDirectionFindTest1(){

        // given
        int menuCode = 34;

        // when
        Menu foundMenu = biDirectionService.findMenu(menuCode);

        // then
        assertEquals(menuCode, foundMenu.getMenuCode());

    }

    @DisplayName("양방향 연관 관계 매핑 조회(연관관계의 주인이 아님)")
    @Test
    void biDirectionFindTest2(){

        // given
        int categoryCode = 123;

        // when
        Category foundCategory = biDirectionService.findCategory(categoryCode);

        // then
        assertEquals(categoryCode, foundCategory.getCategoryCode());

    }

}
