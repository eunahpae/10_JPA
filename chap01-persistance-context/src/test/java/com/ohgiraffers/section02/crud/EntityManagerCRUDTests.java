package com.ohgiraffers.section02.crud;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * EntityManagerCRUD 클래스의 CRUD 기능을 테스트하는 클래스
 */
public class EntityManagerCRUDTests {

    private EntityManagerCRUD crud;

    /**
     * 각 테스트 실행 전마다 EntityManagerCRUD 인스턴스를 새로 생성하여 초기화
     */
    @BeforeEach
    void initManager() {
        this.crud = new EntityManagerCRUD();
    }

    /**
     * 메뉴 코드를 이용한 메뉴 조회 테스트
     *
     * - ParameterizedTest를 사용해 여러 케이스를 반복 실행 - menuCode로 메뉴를 조회한 뒤, 기대한 menuCode와 일치하는지 검증
     *
     * @param menuCode 조회에 사용할 메뉴 코드
     * @param expected 기대하는 menuCode 값 (검증 대상)
     */
    @ParameterizedTest
    @CsvSource({"1,1", "2,2", "3,3"}) // 각 행은 하나의 테스트 케이스
    @DisplayName("메뉴 코드로 메뉴 조회 확인")
    void testFindMenuByMenuCode(int menuCode, int expected) {

        // given: 파라미터로 입력값이 주어지므로 별도 설정 없음

        // when: 메뉴 코드로 메뉴 조회
        Menu foundMenu = crud.findMenuByMenuCode(menuCode);

        // then: 조회 결과의 menuCode가 기대한 값과 일치하는지 확인
        assertEquals(expected, foundMenu.getMenuCode());

        // 로그 출력 (테스트 디버깅용)
        System.out.println("foundMenu = " + foundMenu);
    }

    /**
     * 새로운 메뉴 데이터를 제공하는 메서드
     *
     * - @MethodSource에서 사용될 테스트 입력값 스트림 생성 - 각 Arguments 객체는 하나의 테스트 케이스에 해당
     *
     * @return 새로운 메뉴 데이터를 담은 Arguments 스트림
     */
    private static Stream<Arguments> newMenu() {
        return Stream.of(
            Arguments.of(
                "신메뉴",     // 메뉴 이름
                35000,       // 가격
                4,           // 카테고리 코드
                'Y'          // 주문 가능 여부
            )
        );
    }

    /**
     * 새로운 메뉴를 추가하고 전체 메뉴 개수를 확인하는 테스트
     *
     * - 메뉴를 저장한 후, 전체 개수를 반환하여 기대값과 비교함 - 등록 후 메뉴 총 개수가 22개인지 확인
     *
     * @param menuName        저장할 메뉴 이름
     * @param menuPrice       저장할 메뉴 가격
     * @param categoruCode    저장할 카테고리 코드 (오타: categoryCode가 맞음)
     * @param orderableStatus 주문 가능 여부
     */
    @DisplayName("새로운 메뉴 추가 테스트")
    @ParameterizedTest
    @MethodSource("newMenu")
    void testRegist(String menuName, int menuPrice, int categoruCode, char orderableStatus) {

        // given: 파라미터로 입력된 데이터를 이용하여 새 메뉴 생성
        Menu newMenu = new Menu(menuName, menuPrice, categoruCode, orderableStatus);

        // when: 메뉴를 저장하고 전체 메뉴 개수 반환
        Long count = crud.saveAndReturnAllCount(newMenu);

        // then: 전체 메뉴 수가 22개인지 확인
        assertEquals(22, count);
    }

    /**
     * 메뉴 이름을 수정하고 그 결과를 검증하는 테스트
     *
     * - 지정된 메뉴 코드(menuCode)에 해당하는 메뉴의 이름을 변경한 후
     * - 수정된 메뉴 객체의 이름이 기대한 값(menuName)과 일치하는지 확인
     *
     * @param menuCode  수정할 메뉴의 식별자(PK)
     * @param menuName  변경할 메뉴 이름
     */
    @DisplayName("메뉴 이름 수정 테스트")
    @ParameterizedTest
    @CsvSource("1, 변경된 이름")
    void testModifyMenuName(int menuCode, String menuName) {

        // when: 메뉴 이름 수정 실행
        Menu modifiedMenu = crud.modifyMenuName(menuCode, menuName);

        // then: 수정된 메뉴 이름이 기대한 값과 일치하는지 확인
        assertEquals(menuName, modifiedMenu.getMenuName());
    }


}
