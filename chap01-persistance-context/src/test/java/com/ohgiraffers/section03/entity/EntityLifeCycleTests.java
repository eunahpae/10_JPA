package com.ohgiraffers.section03.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * JPA 엔티티 생명주기(Entity Life Cycle)에 대한 통합 테스트 클래스
 *
 * JPA 에서 엔티티 객체는 영속성 컨텍스트와의 관계에 따라 4가지 상태로 구분된다:
 *
 * 1. 비영속 (Transient)
 * - 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태
 * - 단순한 자바 객체로 DB와 무관한 상태
 * - EntityManager.contains(entity) = false
 *
 * 2. 영속 (Persistent/Managed)
 * - 영속성 컨텍스트에 의해 관리되는 상태
 * - 변경 감지(Dirty Checking) 가능
 * - flush() 또는 commit() 시 DB에 변경사항 반영
 * - EntityManager.contains(entity) = true
 *
 * 3. 준영속 (Detached)
 * - 영속성 컨텍스트에 저장되었다가 분리된 상태
 * - 변경 감지 불가능 (변경해도 DB에 반영되지 않음)
 * - detach(), clear(), close() 메서드로 전환 가능
 * - merge()를 통해 다시 영속 상태로 전환 가능
 *
 * 4. 삭제 (Removed)
 * - 삭제하기로 예약된 상태
 * - remove() 호출 후 상태
 * - commit() 시 DELETE SQL 실행
 *
 *
 * JPA EntityManager의 핵심 메소드
 *
 * 1. detach(entity):
 *    - 특정 엔티티를 영속성 컨텍스트에서 분리
 *    - 준영속 상태로 전환 (변경 감지 비활성화)
 *    - 해당 엔티티의 변경사항은 더 이상 DB에 반영되지 않음
 *
 * 2. merge(entity):
 *    - 준영속 또는 비영속 엔티티를 영속성 컨텍스트에 병합
 *    - 새로운 영속 상태 엔티티 반환 (원본 객체는 여전히 준영속)
 *    - 변경 감지 재활성화
 *
 * 3. flush():
 *    - 영속성 컨텍스트의 변경사항을 DB에 즉시 반영
 *    - 트랜잭션은 유지됨 (commit과 다름)
 *    - 준영속 엔티티의 변경사항은 반영되지 않음
 *
 * 4. commit():
 *    - 트랜잭션 종료 및 flush() 자동 호출
 *    - 모든 변경사항을 DB에 최종 반영
 *
 * 5. clear():
 *    - 영속성 컨텍스트 완전 초기화
 *    - 관리 중인 모든 엔티티가 준영속 상태로 전환
 *    - EntityManager는 계속 사용 가능 (detach 와의 차이점)
 *
 * 6. close():
 *    - 영속성 컨텍스트 완전 종료
 *    - EntityManager 사용 불가 상태로 전환
 *    - 이후 모든 작업에서 IllegalStateException 발생
 *
 * 7. remove():
 *    - 엔티티를 삭제 예약 상태(Removed)로 전환
 *    - commit() 시 DELETE SQL 쿼리 실행
 *    - 트랜잭션 내에서만 의미 있음
 */
public class EntityLifeCycleTests {

    /**
     * 테스트에 사용할 EntityLifeCycle 인스턴스
     */
    private EntityLifeCycle lifeCycle;

    /**
     * 각 테스트 메서드 실행 전 공통 설정 EntityLifeCycle 인스턴스를 새로 생성하여 깨끗한 상태로 테스트 시작
     */
    @BeforeEach
    void setup() {
        this.lifeCycle = new EntityLifeCycle();
    }

    /**
     * 비영속 상태(Transient State) 테스트
     *
     * 목적: 영속 상태 엔티티와 동일한 값을 가진 새로운 객체가 영속성 컨텍스트에 등록되지 않은 비영속 상태임을 검증
     *
     * 테스트 시나리오:
     * 1. DB에서 특정 menuCode로 Menu 엔티티 조회 (영속 상태)
     * 2. 조회된 엔티티의 모든 필드 값을 복사하여 새로운 Menu 객체 생성 (비영속 상태)
     * 3. 두 객체가 같은 데이터를 가지지만 서로 다른 인스턴스인지 확인
     * 4. 첫 번째 객체는 EntityManager가 관리하는지 확인
     * 5. 두 번째 객체는 EntityManager가 관리하지 않는지 확인
     *
     * 핵심 포인트:
     * - 같은 값을 가진 객체라도 영속성 컨텍스트에 등록되지 않으면 비영속 상태
     * - EntityManager.contains()로 영속성 여부 확인 가능
     */
    @DisplayName("비영속 상태(Transient) 테스트")
    @ParameterizedTest
    @ValueSource(ints = {2})
    void testTransient(int menuCode) {

        // when - 테스트 데이터 준비
        // 1. DB에서 menuCode에 해당하는 Menu 엔티티 조회 (영속 상태로 로드됨)
        Menu foundMenu = lifeCycle.findMenuByMenuCode(menuCode);

        // 2. 조회된 엔티티와 동일한 필드 값으로 새로운 Menu 객체 생성 (비영속 상태)
        //    주의: 이는 단순한 생성자 호출이므로 영속성 컨텍스트와 무관
        Menu newMenu = new Menu(
            foundMenu.getMenuCode(),    // 동일한 PK
            foundMenu.getMenuName(),    // 동일한 이름
            foundMenu.getMenuPrice(),   // 동일한 가격
            foundMenu.getCategoryCode(), // 동일한 카테고리
            foundMenu.getOrderableStatus() // 동일한 주문 가능 여부
        );

        // 3. 영속성 컨텍스트 상태 확인을 위한 EntityManager 참조 획득
        EntityManager entityManager = lifeCycle.getManagerInstance();

        // then - 검증
        // 객체 동일성 검증: 같은 데이터를 가져도 서로 다른 인스턴스
        assertNotEquals(foundMenu, newMenu,
            "같은 데이터를 가진 객체라도 서로 다른 인스턴스여야 함");

        // 영속성 상태 검증: foundMenu는 EntityManager에 의해 관리됨
        assertTrue(entityManager.contains(foundMenu),
            "DB에서 조회한 foundMenu는 영속성 컨텍스트에 의해 관리되어야 함");

        // 비영속 상태 검증: newMenu는 영속성 컨텍스트에 등록되지 않음
        assertFalse(entityManager.contains(newMenu),
            "새로 생성한 newMenu는 영속성 컨텍스트에 등록되지 않은 비영속 상태여야 함");
    }

    /**
     * 서로 다른 EntityManager 간의 엔티티 영속성 테스트
     *
     * 목적: 동일한 PK를 가진 엔티티라도 서로 다른 EntityManager가 관리하면 별개의 객체로 인식됨을 검증
     *
     * 테스트 시나리오:
     * 1. 첫 번째 EntityManager로 특정 menuCode의 Menu 엔티티 조회
     * 2. 두 번째 EntityManager로 동일한 menuCode의 Menu 엔티티 조회
     * 3. 두 객체가 서로 다른 인스턴스인지 확인
     *
     * 핵심 포인트:
     * - 영속성 컨텍스트는 EntityManager 단위로 독립적으로 관리됨
     * - 같은 PK의 엔티티라도 다른 EntityManager가 조회하면 다른 객체
     * - 이는 JPA 의 1차 캐시가 EntityManager 레벨에서 동작하기 때문
     */
    @DisplayName("서로 다른 EntityManager의 엔티티 독립성 테스트 - 같은 PK라도 다른 영속성 컨텍스트면 다른 객체")
    @ParameterizedTest
    @ValueSource(ints = {3})
    void testManagedOtherEntityManager(int menuCode) {

        // when - 서로 다른 EntityManager로 동일한 엔티티 조회
        // 1. 첫 번째 EntityManager를 통해 Menu 조회 (새로운 영속성 컨텍스트 1)
        //    lifeCycle.findMenuByMenuCode() 내부에서 새 EntityManager 생성
        Menu menu1 = lifeCycle.findMenuByMenuCode(menuCode);

        // 2. 두 번째 EntityManager를 통해 같은 PK의 Menu 조회 (새로운 영속성 컨텍스트 2)
        //    lifeCycle.findMenuByMenuCode() 내부에서 또 다른 새 EntityManager 생성
        Menu menu2 = lifeCycle.findMenuByMenuCode(menuCode);

        // then - 검증
        // 동일한 PK를 가진 엔티티라도 서로 다른 EntityManager에서 조회하면 다른 객체
        assertNotEquals(menu1, menu2,
            "서로 다른 EntityManager가 관리하는 엔티티는 같은 PK라도 다른 인스턴스여야 함");

        // 참고: menu1과 menu2는 DB 에서는 같은 레코드를 참조하지만, 메모리상에서는 완전히 별개의 객체로 존재함
    }

    /**
     * 동일한 EntityManager 내에서의 엔티티 동일성 테스트 (1차 캐시 동작 확인)
     *
     * 목적: 같은 EntityManager로 동일한 PK의 엔티티를 여러 번 조회할 때 JPA 1차 캐시에 의해 동일한 객체가 반환됨을 검증
     *
     * 테스트 시나리오:
     * 1. 하나의 EntityManager 인스턴스 생성
     * 2. 동일한 menuCode로 Menu 엔티티 두 번 조회
     * 3. 두 조회 결과가 완전히 동일한 객체인지 확인
     *
     * 핵심 포인트:
     * - JPA 의 1차 캐시: 영속성 컨텍스트 내에서 동일 PK 엔티티는 하나만 존재
     * - 두 번째 조회부터는 DB 접근 없이 캐시에서 반환
     * - 동일성(identity) 보장: == 비교도 true
     * - 성능 최적화: 불필요한 DB 쿼리 방지
     */
    @DisplayName("동일 EntityManager의 1차 캐시 테스트 - 같은 PK 조회 시 동일한 객체 반환")
    @ParameterizedTest
    @ValueSource(ints = {3})
    void testManagedSameEntityManager(int menuCode) {

        // given - 테스트용 EntityManager 준비
        // 동일한 EntityManager 인스턴스를 사용하여 1차 캐시 동작 확인
        EntityManager entityManager = EntityManagerGenerator.getInstance();

        // when - 같은 EntityManager로 동일한 PK의 엔티티 두 번 조회
        // 첫 번째 조회: DB 에서 데이터를 읽어와 1차 캐시에 저장
        Menu menu1 = entityManager.find(Menu.class, menuCode);

        // 두 번째 조회: DB 접근 없이 1차 캐시에서 동일한 객체 반환
        Menu menu2 = entityManager.find(Menu.class, menuCode);

        // then - 검증
        // JPA 1차 캐시에 의해 완전히 동일한 객체 반환 (참조값까지 같음)
        assertEquals(menu1, menu2,
            "같은 EntityManager에서 동일한 PK로 조회한 엔티티는 동일한 객체여야 함");

        // 추가 검증: 참조 동일성까지 확인 (== 연산자로도 true)
        assertTrue(menu1 == menu2,
            "1차 캐시에 의해 참조값까지 동일해야 함");
    }

    /**
     * 준영속 상태(Detached State) 동작 테스트
     *
     * 목적: detach된 엔티티의 변경사항이 DB에 반영되지 않음을 검증
     *
     * 테스트 시나리오:
     * 1. 트랜잭션 시작 및 영속 상태 엔티티 조회
     * 2. detach()로 영속성 컨텍스트에서 분리 (준영속 상태 전환)
     * 3. 준영속 엔티티의 필드 값 변경
     * 4. flush() 호출 (변경사항이 반영되지 않음)
     * 5. DB에서 재조회하여 변경사항이 반영되지 않았는지 확인
     *
     * 핵심 포인트:
     *
     * - 준영속 엔티티는 변경 감지(Dirty Checking) 대상에서 제외
     * - flush() 호출해도 준영속 엔티티의 변경사항은 무시됨
     * - 성능상 이점: 불필요한 업데이트 방지
     */
    @DisplayName("준영속 상태 테스트 - detach된 엔티티의 변경사항은 DB에 반영되지 않음")
    @ParameterizedTest
    @CsvSource("11, 1000")
    void testDetachEntity(int menuCode, int menuPrice) {
        // given - 테스트 환경 설정
        EntityManager entityManager = EntityManagerGenerator.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        // when - 테스트 시나리오 수행
        entityTransaction.begin(); // 트랜잭션 시작

        // 1. DB에서 Menu 엔티티 조회 (영속 상태)
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        // 2. 영속성 컨텍스트에서 분리 (영속 → 준영속)
        entityManager.detach(foundMenu);

        // 3. 준영속 상태에서 필드 값 변경 (변경 감지 대상 아님)
        foundMenu.setMenuPrice(menuPrice);

        // 4. 영속성 컨텍스트의 변경사항을 DB에 반영 시도
        //    하지만 foundMenu는 준영속 상태이므로 변경사항 무시됨
        entityManager.flush();

        // then - 검증
        // DB에서 재조회한 값이 변경되지 않았는지 확인
        Menu reloadedMenu = entityManager.find(Menu.class, menuCode);
        assertNotEquals(menuPrice, reloadedMenu.getMenuPrice(),
            "준영속 상태 엔티티의 변경사항은 DB에 반영되지 않아야 함");

        // 트랜잭션 정리 (실제로는 변경사항이 없으므로 commit/rollback 무관)
        entityTransaction.rollback();
    }

    /**
     * 준영속 상태에서 병합(Merge)을 통한 영속성 복원 테스트
     *
     * 목적: detach된 엔티티를 merge를 통해 다시 영속 상태로 전환하고 변경사항이 DB에 반영됨을 검증
     *
     * 테스트 시나리오:
     * 1. 영속 상태 엔티티 조회 후 detach (준영속 전환)
     * 2. 준영속 엔티티의 필드 값 변경
     * 3. merge() 호출로 변경사항을 포함한 새로운 영속 엔티티 생성
     * 4. flush()로 변경사항 DB 반영
     *
     * 5. DB에서 재조회하여 변경사항이 반영되었는지 확인
     *
     * 핵심 포인트:
     * - merge()는 새로운 영속 엔티티를 반환 (원본은 여전히 준영속)
     * - 병합된 엔티티는 변경 감지 활성화
     * - 준영속 → 영속 상태 전환의 표준 방법
     */
    @DisplayName("준영속 상태 병합 테스트 - detach 후 merge로 영속성 복원")
    @ParameterizedTest
    @CsvSource("11, 1000")
    void testDetachAndMerge(int menuCode, int menuPrice) {
        // given - 테스트 환경 설정
        EntityManager entityManager = EntityManagerGenerator.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        // when - 테스트 시나리오 수행
        entityTransaction.begin(); // 트랜잭션 시작

        // 1. 영속 상태 엔티티 조회
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        // 2. 영속성 컨텍스트에서 분리 (영속 → 준영속)
        entityManager.detach(foundMenu);

        // 3. 준영속 상태에서 필드 값 변경
        foundMenu.setMenuPrice(menuPrice);

        // 4. 준영속 엔티티를 영속성 컨텍스트에 병합
        //    변경사항이 반영된 새로운 영속 엔티티 생성 및 반환
        Menu mergedMenu = entityManager.merge(foundMenu);

        // 5. 영속 상태 엔티티의 변경사항을 DB에 반영
        entityManager.flush();

        // then - 검증
        // DB 에서 재조회한 값이 변경되었는지 확인
        Menu reloadedMenu = entityManager.find(Menu.class, menuCode);
        assertEquals(menuPrice, reloadedMenu.getMenuPrice(),
            "merge된 엔티티의 변경사항은 DB에 반영되어야 함");

        // 참고: foundMenu(준영속)와 mergedMenu(영속)는 서로 다른 객체
        assertNotEquals(foundMenu, mergedMenu,
            "merge는 새로운 영속 엔티티를 반환함");

        entityTransaction.rollback(); // 테스트 데이터 정리
    }

    /**
     * detach 후 merge를 통한 필드 업데이트 확인 테스트
     *
     * 목적: 트랜잭션 없이도 merge된 엔티티의 변경사항이 영속성 컨텍스트에 올바르게 반영됨을 검증
     *
     * 테스트 시나리오:
     * 1. 영속 상태 엔티티 조회
     * 2. detach로 준영속 상태 전환
     * 3. 메뉴명 변경 후 merge
     * 4. DB에서 재조회하여 변경사항 확인
     *
     * 핵심 포인트:
     *
     * - merge 후 즉시 find()로 조회 시 변경된 값 확인 가능
     * - 영속성 컨텍스트 내에서의 데이터 일관성 보장
     * - 실제 DB 반영은 트랜잭션 commit 시점
     */
    @DisplayName("detach 후 merge한 엔티티의 필드 변경 확인 테스트")
    @ParameterizedTest
    @CsvSource("34, 전복죽")
    void testDetachAndMergeFieldUpdate(int menuCode, String menuName) {
        // given - 테스트 환경 설정
        EntityManager entityManager = EntityManagerGenerator.getInstance();

        // when - 테스트 시나리오 수행
        // 1. 영속 상태 엔티티 조회
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        // 2. 영속성 컨텍스트에서 분리 (영속 → 준영속)
        entityManager.detach(foundMenu);

        // 3. 준영속 상태에서 메뉴명 변경
        foundMenu.setMenuName(menuName);

        // 4. 변경된 준영속 엔티티를 영속성 컨텍스트에 병합
        entityManager.merge(foundMenu);

        // then - 검증
        // 영속성 컨텍스트에서 재조회하여 변경사항 확인
        Menu refoundMenu = entityManager.find(Menu.class, menuCode);
        assertEquals(menuName, refoundMenu.getMenuName(),
            "merge된 엔티티의 필드 변경사항이 영속성 컨텍스트에 반영되어야 함");

        // 참고: 트랜잭션이 활성화되지 않았으므로 실제 DB 에는 아직 반영되지 않음
        //      하지만 영속성 컨텍스트 내에서는 변경된 값으로 관리됨
    }

    /**
     * 영속성 컨텍스트 전체 초기화(clear) 테스트
     *
     * 목적: clear() 호출 시 영속성 컨텍스트의 모든 엔티티가 준영속 상태로 전환됨을 검증
     *
     * 테스트 시나리오:
     * 1. 특정 menuCode로 Menu 엔티티 조회 (영속 상태)
     * 2. EntityManager.clear() 호출 (모든 엔티티 준영속화)
     * 3. 같은 menuCode로 다시 조회
     * 4. 두 객체가 서로 다른 인스턴스인지 확인
     *
     * 핵심 포인트:
     * - clear() 후에는 1차 캐시가 완전히 비워짐
     * - 동일한 PK로 재조회 시 새로운 객체 생성
     *
     * - detach()는 특정 엔티티만, clear()는 전체 엔티티 대상
     * - EntityManager는 계속 사용 가능
     */
    @DisplayName("영속성 컨텍스트 전체 초기화 테스트 - clear() 후 모든 엔티티 준영속화")
    @ParameterizedTest
    @ValueSource(ints = {6})
    void testClearPersistanceContext(int menuCode) {

        // given - 테스트 환경 설정
        EntityManager entityManager = EntityManagerGenerator.getInstance();

        // when - 테스트 시나리오 수행
        // 1. Menu 엔티티 조회하여 영속성 컨텍스트에 로드 (1차 캐시 저장)
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        // 2. 영속성 컨텍스트 전체 초기화 (모든 엔티티 준영속화, 1차 캐시 비움)
        entityManager.clear();

        // 3. 같은 menuCode로 다시 조회 (새로운 DB 쿼리 실행, 새 객체 생성)
        Menu expectedMenu = entityManager.find(Menu.class, menuCode);

        // then - 검증
        // clear() 전후의 객체가 서로 다른 인스턴스인지 확인
        assertNotEquals(foundMenu, expectedMenu,
            "clear() 후 동일한 PK로 재조회한 엔티티는 다른 인스턴스여야 함");

        // 추가 검증 가능 항목들:
        // - foundMenu는 더 이상 영속 상태가 아님
        assertFalse(entityManager.contains(foundMenu),
            "clear() 후 기존 엔티티는 준영속 상태여야 함");

        // - expectedMenu는 새로운 영속 상태 엔티티
        assertTrue(entityManager.contains(expectedMenu),
            "재조회한 엔티티는 영속 상태여야 함");
    }

    /**
     * 영속성 컨텍스트 완전 종료(close) 테스트
     *
     * 목적: close() 호출 시 EntityManager가 완전히 사용 불가 상태가 되어 이후 모든 작업에서 예외가 발생함을 검증
     *
     * 테스트 시나리오:
     * 1. Menu 엔티티 조회 (영속 상태)
     * 2. EntityManager.close() 호출 (영속성 컨텍스트 종료)
     * 3. 종료된 EntityManager로 작업 시도
     * 4. IllegalStateException 발생 확인
     *
     * 핵심 포인트:
     * - close() 후 EntityManager는 완전히 사용 불가
     *
     * - 모든 JPA 작업에서 IllegalStateException 발생
     * - clear()와 달리 EntityManager 자체를 폐기
     * - 리소스 정리 및 메모리 해제
     */
    @DisplayName("영속성 컨텍스트 종료 테스트 - close() 후 EntityManager 사용 불가")
    @ParameterizedTest
    @ValueSource(ints = {6})
    void testClosePersistanceContext(int menuCode) {

        // given - 테스트 환경 설정
        EntityManager entityManager = EntityManagerGenerator.getInstance();

        // when - 테스트 시나리오 수행
        // 1. Menu 엔티티 조회 (정상 동작 확인)
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        // 검증: 조회된 엔티티 존재 확인
        assertNotNull(foundMenu, "close() 전에는 정상적으로 엔티티 조회가 가능해야 함");

        // 2. 영속성 컨텍스트 완전 종료
        entityManager.close();

        // then - 검증
        // close() 후 EntityManager 사용 시 IllegalStateException 발생 확인
        assertThrows(
            IllegalStateException.class,
            () -> entityManager.find(Menu.class, menuCode),
            "close()된 EntityManager 사용 시 IllegalStateException이 발생해야 함"
        );

        // 추가로 다른 작업들도 모두 예외 발생
        assertThrows(IllegalStateException.class,
            () -> entityManager.contains(foundMenu),
            "close()된 EntityManager의 contains() 호출도 예외 발생해야 함");

        assertThrows(IllegalStateException.class,
            () -> entityManager.flush(),
            "close()된 EntityManager의 flush() 호출도 예외 발생해야 함");
    }

    /**
     * 영속 엔티티 삭제(remove) 테스트
     *
     * 목적: remove()를 통해 엔티티를 삭제 예약 상태로 전환하고 flush() 시 실제 DB 에서 삭제됨을 검증
     *
     * 테스트 시나리오:
     * 1. 트랜잭션 시작 및 Menu 엔티티 조회 (영속 상태)
     * 2. remove() 호출로 삭제 예약 (Removed 상태)
     * 3. flush()로 DELETE SQL 실행
     * 4. DB에서 해당 엔티티가 삭제되었는지 확인
     * 5. 테스트 데이터 복구를 위한 rollback
     *
     * 핵심 포인트:
     * - remove()는 즉시 삭제가 아닌 삭제 예약
     * - flush() 또는 commit() 시 실제 DELETE SQL 실행
     * - 삭제된 엔티티 조회 시
     * null 반환
     * - 트랜잭션 내에서만 의미 있는 작업
     * - cascade 설정 시 연관 엔티티도 함께 삭제 가능
     */
    @DisplayName("영속 엔티티 삭제 테스트 - remove() 후 flush()로 실제 DB 삭제")
    @ParameterizedTest
    @ValueSource(ints = {6})
    void testRemoveEntity(int menuCode) {

        // given - 테스트 환경 설정
        EntityManager entityManager = EntityManagerGenerator.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        // when - 테스트 시나리오 수행
        entityTransaction.begin(); // 트랜잭션 시작 (삭제 작업은 트랜잭션 필수)

        // 1. 삭제할 Menu 엔티티 조회 (영속 상태)
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        // 전제 조건 검증: 엔티티가 존재해야 함
        assertNotNull(foundMenu, "삭제할 엔티티가 존재해야 함");

        // 2. 엔티티를 삭제 예약 상태로 전환 (영속 → 삭제 예약)
        entityManager.remove(foundMenu);

        // 3. 삭제 예약된 엔티티를 실제 DB 에서 삭제 (DELETE SQL 실행)
        entityManager.flush();

        // then - 검증
        // DB 에서 해당 엔티티 재조회 시 null 반환 확인
        Menu refoundMenu = entityManager.find(Menu.class, menuCode);
        assertNull(refoundMenu,
            "remove() 후 flush()된 엔티티는 DB 에서 삭제되어 null 이어야 함");

        // 영속성 컨텍스트에서도 해당 엔티티 관리 중단 확인
        assertTrue(entityManager.contains(foundMenu),
            "remove()된 엔티티는 더 이상 영속성 컨텍스트에서 관리되지 않아야 함");

        // 테스트 데이터 복구를 위한 트랜잭션 롤백
        // 실제 서비스에서는 commit()을 호출하여 삭제를 확정
        entityTransaction.rollback();

        // 롤백 후 엔티티가 복구되었는지 확인 (선택적 검증)
        Menu restoredMenu = entityManager.find(Menu.class, menuCode);
        assertNotNull(restoredMenu,
            "rollback 후에는 삭제된 엔티티가 복구되어야 함");
    }

    /**
     * 준영속 엔티티 삭제 시도 테스트 (예외 상황)
     *
     * 목적: 준영속 상태의 엔티티를 remove() 시도할 때 발생하는 예외 확인
     *
     * 핵심 포인트:
     * - remove()는 영속 상태의 엔티티만 가능
     *
     * - 준영속 엔티티 remove() 시 IllegalArgumentException 발생
     * - 삭제하려면 먼저 merge()로 영속 상태로 전환 필요
     */
    @DisplayName("준영속 엔티티 삭제 예외 테스트 - 준영속 상태 remove() 시 예외 발생")
    @ParameterizedTest
    @ValueSource(ints = {7})
    void testRemoveDetachedEntity(int menuCode) {
        // given
        EntityManager entityManager = EntityManagerGenerator.getInstance();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        // when
        entityTransaction.begin();

        // 1. 영속 상태 엔티티 조회
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        // 2. 준영속 상태로 전환
        entityManager.detach(foundMenu);

        // then
        // 준영속 엔티티 remove() 시도 시 예외 발생 확인
        assertThrows(IllegalArgumentException.class,
            () -> entityManager.remove(foundMenu),
            "준영속 상태 엔티티를 remove() 시도 시 IllegalArgumentException 발생해야 함");

        entityTransaction.rollback();
    }

}
