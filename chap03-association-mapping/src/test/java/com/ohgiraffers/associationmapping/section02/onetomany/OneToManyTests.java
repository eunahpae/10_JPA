package com.ohgiraffers.associationmapping.section02.onetomany;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * OneToMany 연관관계 테스트 클래스
 */
@SpringBootTest
public class OneToManyTests {

    @Autowired
    private OneToManyService oneToManyService;

    // 카테고리 조회 시 연관된 메뉴 리스트도 함께 조회되는지 확인
    @DisplayName("1:N 연관 관계 조회 테스트")
    @Test
    void oneToManyFindTest() {
        // given
        int categoryCode = 1;

        // when
        Category category = oneToManyService.findCategory(categoryCode);

        // then
        assertNotNull(category);
        // 추가 검증: category.getMenuList()도 확인 가능
    }

    private static Stream<Arguments> getMenuInfo() {
        return Stream.of(
            // Arguments: menuCode, menuName, menuPrice, categoryCode, categoryName, orderableStatus
            Arguments.of(321, "스파게티 돈가스", 30000, 321, "분식퓨전", "Y")
        );
    }


     // 카테고리와 해당 카테고리에 속한 메뉴를 함께 저장하는 테스트
    @DisplayName("1:N 연관관계 객체 삽입 테스트")
    @ParameterizedTest
    @MethodSource("getMenuInfo") // getMenuInfo() 메서드에서 테스트 데이터 제공
    void oneToManyInsertTest(
        int menuCode, String menuName, int menuPrice,
        int categoryCode, String categoryName, String orderableStatus
    ) {
        // given
        // 1. 카테고리 정보 생성
        CategoryDTO categoryInfo = new CategoryDTO(
            categoryCode, categoryName, null, null
        );

        // 2. 메뉴 리스트 생성 및 메뉴 정보 추가
        List<MenuDTO> menuList = new ArrayList<>();
        MenuDTO menuInfo = new MenuDTO(
            menuCode, menuName, menuPrice, categoryCode, orderableStatus
        );
        menuList.add(menuInfo);

        // 3. 카테고리에 메뉴 리스트 설정 (일대다 관계 설정)
        categoryInfo.setMenuList(menuList);

        // when
        // then
        // OneToMany cascade 설정으로 인해 메뉴도 함께 저장됨
        Assertions.assertDoesNotThrow(
            () -> oneToManyService.registCategory(categoryInfo)
        );
    }
}