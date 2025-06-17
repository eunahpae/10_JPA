package com.ohgiraffers.section02.crud;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * EntityManagerCRUD 클래스의 CRUD 기능 테스트
 */
public class EntityManagerCRUDTests {

    private EntityManagerCRUD crud;

    /**
     * 각 테스트 실행 전마다 EntityManagerCRUD 인스턴스를 새로 생성하여 초기화
     */
    @BeforeEach
    void initManager(){
        this.crud = new EntityManagerCRUD();
    }

    /**
     * 파라미터로 주어진 메뉴 코드로 메뉴를 조회하고,
     * 기대한 코드 값과 일치하는지 검증하는 테스트
     *
     * @param menuCode   조회할 메뉴 코드
     * @param expected   기대하는 메뉴 코드 값
     */
    // @Test
    @ParameterizedTest
    @CsvSource({"1,1", "2,2", "3,3"}) // 파라미터화된 테스트: 각 케이스를 순차적으로 실행
    @DisplayName("메뉴 코드로 메뉴 조회 확인")
    void testFindMenuByMenuCode(int menuCode, int expected){

        // given: 테스트 입력값(menuCode)은 파라미터로 주어지므로 임의값 주석처리
        // int menuCode = 1;

        // when: 메뉴 코드로 메뉴 객체 조회
        Menu foundMenu = crud.findMenuByMenuCode(menuCode);

        // then: 조회된 메뉴의 menuCode가 기대한 값과 일치하는지 검증
        assertEquals(expected, foundMenu.getMenuCode());

        // 테스트 결과 확인을 위한 출력 (선택사항)
        System.out.println("foundMenu = " + foundMenu);
    }

}
