package com.ohgiraffers.section03.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * JPA 엔티티 생명주기(Entity Life Cycle)에 대한 테스트 클래스
 *
 * - JPA 에서는 엔티티 객체가 어떤 상태에 있는지에 따라 동작이 달라짐
 * - 엔티티 상태는 크게 4가지로 나뉨:
 *                             1. 비영속 (Transient)    : 엔티티가 영속성 컨텍스트에 저장되기 전 상태 (DB와 무관)
 *                             2. 영속   (Persistent)  : 영속성 컨텍스트에 저장된 상태 (변경감지 가능, flush/commit 시 DB 반영)
 *                             3. 준영속 (Detached)    : 영속성 컨텍스트에서 분리된 상태 (DB와 연결 끊김)
 *                             4. 삭제   (Removed)    : 삭제가 예약된 상태 (commit 시 delete SQL 실행)
 */
public class EntityLifeCycleTests {

    private EntityLifeCycle lifeCycle;

    /**
     * 각 테스트 실행 전 EntityLifeCycle 인스턴스를 초기화
     */
    @BeforeEach
    void setup() {
        this.lifeCycle = new EntityLifeCycle();
    }

    /**
     * 비영속 상태 테스트
     *
     * - 기존의 영속 상태 엔티티를 기준으로, 동일한 값으로 새로 생성한 객체가 영속성 컨텍스트에 등록되지 않았음을 검증.
     *
     * 테스트 흐름: 1. 특정 menuCode로 영속 상태 엔티티를 조회 (foundMenu)
     *           2. foundMenu의 필드 값을 복사하여 새로운 객체(newMenu) 생성
     *           3. 두 객체는 같은 값을 갖더라도 서로 다른 인스턴스이므로 다르다 (assertNotEquals)
     *           4. foundMenu는 EntityManager가 관리 중인 영속객체이다 (assertTrue)
     *           5. newMenu는 단순한 자바 객체로, 영속성 컨텍스트에 등록되지 않은 비영속 객체이다 (assertFalse)
     */
    @DisplayName("비영속 테스트")
    @ParameterizedTest
    @ValueSource(ints = {2})
    void testTransient(int menuCode) {

        // when
        // 1. 특정 menuCode로 DB 에서 Menu 엔티티를 조회 (영속 상태 객체)
        Menu foundMenu = lifeCycle.findMenuByMenuCode(menuCode);

        // 2. 동일한 필드 값을 가진 새로운 Menu 객체를 수동으로 생성 (비영속 객체)
        Menu newMenu = new Menu(
            foundMenu.getMenuCode(),
            foundMenu.getMenuName(),
            foundMenu.getMenuPrice(),
            foundMenu.getCategoryCode(),
            foundMenu.getOrderableStatus());

        // 3. 영속성 컨텍스트 확인용 EntityManager 획득
        EntityManager entityManager = lifeCycle.getManagerInstance();

        // then
        // 서로 다른 인스턴스이므로 equals 비교 결과는 false (identity 기준)
        assertNotEquals(foundMenu, newMenu);

        // foundMenu는 entityManager에 의해 관리 중인 영속 객체
        assertTrue(entityManager.contains(foundMenu));

        // newMenu는 영속성 컨텍스트에 등록되지 않은 비영속 객체
        assertFalse(entityManager.contains(newMenu));
    }

    /**
     * 서로 다른 EntityManager가 관리하는 동일 엔티티의 영속성 테스트
     *
     * - 동일한 식별자(PK)를 가진 엔티티라도, 서로 다른 EntityManager에서 조회하면 각각 독립된 영속성 컨텍스트에서 관리되는 별개의 객체로 간주됨.
     *
     * 테스트 흐름: 1. menuCode를 기반으로 두 번의 엔티티 조회 수행
     *            - EntityLifeCycle.findMenuByMenuCode() 호출 시마다 새로운 EntityManager 인스턴스가 생성된다고 가정
     *           2. menu1과 menu2는 동일한 식별자 값을 갖더라도 서로 다른 영속성 컨텍스트에 의해 관리되므로 두 객체는 다른 인스턴스임
     *           3. 따라서 assertNotEquals를 통해 동일하지 않음을 검증
     *
     * 영속성 컨텍스트는 EntityManager 단위로 구분되며, 같은 PK의 엔티티라 해도 EntityManager가 다르면 관리 주체가 다르다.
     */
    @DisplayName("다른 엔티티 매니저가 관리하는 엔티티의 영속성 테스트")
    @ParameterizedTest
    @ValueSource(ints = {3})
    void testManagedOtherEntityManager(int menuCode) {

        // when
        // 1. 첫 번째 EntityManager를 통해 menuCode에 해당하는 Menu 조회 (영속 객체 1)
        Menu menu1 = lifeCycle.findMenuByMenuCode(menuCode);

        // 2. 두 번째 EntityManager를 통해 같은 menuCode의 Menu를 다시 조회 (영속 객체 2)
        Menu menu2 = lifeCycle.findMenuByMenuCode(menuCode);

        // then
        // menu1과 menu2는 DB 상 동일한 데이터를 참조하지만,
        // 각각 다른 EntityManager가 관리하므로 서로 다른 인스턴스로 존재
        assertNotEquals(menu1, menu2);
    }


    /**
     * 같은 EntityManager가 관리하는 동일 엔티티의 영속성 테스트
     * - 동일한 식별자(PK)를 가진 엔티티를 같은 EntityManager로 두 번 조회하면, JPA는 1차 캐시에서 해당 엔티티를 재사용하므로 동일한 인스턴스를 반환함
     *
     * 테스트 흐름: 1. 테스트 전용 EntityManager 인스턴스를 획득
     *           2. 같은 menuCode를 기준으로 Menu 엔티티를 두 번 조회 → 두 번째 조회 시PA는 DB를 조회하지 않고, 1차 캐시에 존재하는 엔티티를 반환
     *           3. 두 엔티티는 참조값이 같은 동일 객체임을 assertEquals로 검증
     *
     * JPA의 영속성 컨텍스트는 동일한 식별자의 엔티티는 단 하나의 인스턴스만 관리한다는 점을 보여줌
     */
    @DisplayName("같은 엔티티 매니저가 관리하는 엔티티의 영속성 테스트")
    @ParameterizedTest
    @ValueSource(ints = {3})
    void testManagedSameEntityManager(int menuCode) {

        // given
        // 테스트에서 사용할 동일한 EntityManager 인스턴스를 준비
        EntityManager entityManager = EntityManagerGenerator.getInstance();

        // when
        // 같은 PK로 두 번 조회하되, 같은 EntityManager를 사용
        Menu menu1 = entityManager.find(Menu.class, menuCode); // 첫 번째 조회 → DB 접근
        Menu menu2 = entityManager.find(Menu.class, menuCode); // 두 번째 조회 → 1차 캐시에서 반환

        // then
        // JPA는 같은 영속성 컨텍스트 내에서 동일한 식별자를 가진 엔티티는 단 하나의 인스턴스로 유지함
        // 따라서 두 객체는 참조값까지 동일한 같은 객체
        assertEquals(menu1, menu2);
    }

    /**
     * JPA 엔티티 생명주기 중 준영속 상태와 관련된 테스트들
     *
     * - detach() : 영속성 컨텍스트에서 특정 엔티티를 분리하여 준영속 상태로 만듦
     * - merge()  : 준영속 상태의 엔티티를 다시 영속 상태로 병합
     *
     * 영속성 컨텍스트와 DB 반영 관계를 테스트하며, flush와 commit 차이도 간접적으로 이해할 수 있음
     */
    public class EntityLifeCycleDetachMergeTests {

        /**
         * 준영속화(detach) 테스트
         *
         * - 특정 엔티티를 영속성 컨텍스트에서 분리(detach)한 후 값을 변경해도, 해당 변경은 DB에 반영되지 않음을 검증하는 테스트
         *
         * 테스트 흐름: 1. EntityManager와 트랜잭션 시작
         *           2. menuCode로 Menu 엔티티를 조회 → 영속 상태 엔티티 획득
         *           3. entityManager.detach()를 호출해 영속성 컨텍스트에서 분리 → 준영속 상태로 전환
         *           4. 준영속 상태 객체의 필드를 변경해도 JPA는 더 이상 이 객체를 관리하지 않음
         *           5. flush() 실행 시 변경 감지는 영속 상태에서만 일어나므로 DB에 update 쿼리 실행 안 됨
         *           6. DB에서 다시 조회 시 변경 전 값이 유지됨을 확인
         *
         * flush는 영속성 컨텍스트의 변경 사항을 DB에 반영하지만, 준영속 상태의 엔티티는 대상이 아니므로 변경 내용은 반영되지 않음
         * flush 대신 commit()을 호출해도 결과는 동일  → commit은 내부적으로 flush를 호출하지만, 변경 감지는 오직 영속 상태 엔티티에서만 작동하기 때문이다.
         */
        @DisplayName("준영속화 detach 테스트")
        @ParameterizedTest
        @CsvSource("11, 1000")
        void testDetachEntity(int menuCode, int menuPrice) {

            // given
            EntityManager entityManager = EntityManagerGenerator.getInstance();
            EntityTransaction entityTransaction = entityManager.getTransaction();

            // when
            entityTransaction.begin();

            // 1. 영속 상태 엔티티 조회
            Menu foundMenu = entityManager.find(Menu.class, menuCode);

            // 2. detach 호출로 준영속 상태로 변경
            entityManager.detach(foundMenu);

            // 3. 준영속 객체의 필드 값 변경 (변경 감지 대상 아님)
            foundMenu.setMenuPrice(menuPrice);

            // 4. flush 호출해도 변경 내용은 DB에 반영되지 않음
            entityManager.flush();

            // then
            // DB에서 다시 조회한 값은 변경 전 값 유지
            assertNotEquals(menuPrice, entityManager.find(Menu.class, menuCode).getMenuPrice());
        }

        /**
         * 준영속화(detach) 후 다시 영속화(merge) 테스트
         *
         * - detach로 인해 준영속 상태가 된 엔티티를 merge()로 다시 영속 상태로 병합함
         * - 병합 과정에서 변경된 필드 값도 함께 병합되어 flush나 commit 시 update 쿼리 실행됨
         *
         * 테스트 흐름: 1. menuCode로 엔티티 조회 → 영속 상태
         *           2. detach() 호출로 준영속 상태 전환
         *           3. 준영속 객체 필드 변경
         *           4. merge() 호출 → 준영속 객체가 다시 영속 상태 엔티티로 병합됨
         *           5. flush() 호출 → 병합된 변경 내용 DB 반영
         *           6. DB에서 조회 시 변경 값이 반영되어 있음 검증
         */
        @DisplayName("준영속화 detach 후 다시 영속화 테스트")
        @ParameterizedTest
        @CsvSource("11, 1000")
        void testDetachAndMerge(int menuCode, int menuPrice) {

            // given
            EntityManager entityManager = EntityManagerGenerator.getInstance();
            EntityTransaction entityTransaction = entityManager.getTransaction();

            // when
            entityTransaction.begin();

            // 1. 영속 상태 엔티티 조회
            Menu foundMenu = entityManager.find(Menu.class, menuCode);

            // 2. detach 수행하여 준영속 상태로 변경
            entityManager.detach(foundMenu);

            // 3. 준영속 상태 객체 필드 변경
            foundMenu.setMenuPrice(menuPrice);

            // 4. merge 호출 → 준영속 객체가 다시 영속 상태로 병합됨
            entityManager.merge(foundMenu);

            // 5. flush 호출 → 변경 내용 DB에 반영
            entityManager.flush();

            // then
            // DB에서 다시 조회 시 변경된 값이 반영되어 있음
            assertEquals(menuPrice, entityManager.find(Menu.class, menuCode).getMenuPrice());
        }

        /**
         * detach 후 merge 한 엔티티 필드 업데이트 테스트
         *
         * - detach 후 필드를 변경하고 merge 수행 시, 변경 내용이 DB에 반영되는지 확인
         *
         * 테스트 흐름: 1. 특정 menuCode로 영속 상태 엔티티 조회
         *           2. detach 호출로 준영속 상태 전환
         *           3. 필드(menuName) 변경
         *           4. merge 호출로 준영속 엔티티를 다시 영속 상태로 병합
         *           5. 병합된 엔티티로 조회하여 변경 내용이 반영되었는지 검증
         */
        @DisplayName("detach 후 merge 한 데이터 update 테스트")
        @ParameterizedTest
        @CsvSource("34, 전복죽")
        void testDetachAndMerge(int menuCode, String menuName) {

            // given
            EntityManager entityManager = EntityManagerGenerator.getInstance();

            // 1. 영속 상태 엔티티 조회
            Menu foundMenu = entityManager.find(Menu.class, menuCode);

            // 2. detach 수행 → 준영속 상태 전환
            entityManager.detach(foundMenu);

            // when
            // 3. 필드 변경 (영속성 컨텍스트가 추적하지 않는 상태)
            foundMenu.setMenuName(menuName);

            // 4. 병합 호출
            entityManager.merge(foundMenu);

            // then
            // 5. 병합 후 다시 조회하여 변경 반영 확인
            Menu refoundMenu = entityManager.find(Menu.class, menuCode);
            assertEquals(menuName, refoundMenu.getMenuName());
        }
    }

}
