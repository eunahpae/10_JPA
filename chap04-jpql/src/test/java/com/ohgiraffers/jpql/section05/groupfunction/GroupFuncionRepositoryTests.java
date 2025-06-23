package com.ohgiraffers.jpql.section05.groupfunction;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GroupFuncionRepositoryTests {

    @Autowired
    private GroupFunctionRepository groupFunctionRepository;

    @DisplayName("특정 카테고리에 등록된 메뉴 수 조회")
    @Test
    void testCountMenuOfCategory() {
        // given
        int categoryCode = 4;

        // when
        long CountOfMenu = groupFunctionRepository.countMenuOfCategory(categoryCode);

        // then
        assertTrue(categoryCode > 0);
        System.out.println("CountOfMenu = " + CountOfMenu);
    }

    @DisplayName("COUNT 외의 다른 그룹 함수 조회 결과가 없는 경우")
    @Test
    void testOtherWithNoResult() {
        // given
        int categoryCode = 50;

        // when
        // then
        assertDoesNotThrow(
            () -> {
                Long SumOfMenu = groupFunctionRepository.otherWithNoResult(categoryCode);
                System.out.println("SumOfMenu = " + SumOfMenu);
            }
        );
    }

    @DisplayName("GROUP BY절과 HAVING절을 사용한 조회 테스트")
    @Test
    public void testSelectByGroupbyHaving() {
        // given
        long minPrice = 50000L;

        // when
        List<Object[]> sumPriceOfCategoryList = groupFunctionRepository.selectByGroupByHaving(minPrice);

        // then
        assertNotNull(sumPriceOfCategoryList);
        sumPriceOfCategoryList.forEach(
            row -> {
                for (Object column : row) {
                    System.out.print(column + " ");
                }
                System.out.println();
            }
        );

    }

    }
