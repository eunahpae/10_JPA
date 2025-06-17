package com.ohgiraffers.section02.crud;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * EntityManager를 이용한 기본 CRUD 기능 제공 클래스
 */
public class EntityManagerCRUD {

    private EntityManager entityManager;

    /**
     * 1. 특정 메뉴 코드로 메뉴 조회
     * <p>
     * - EntityManager의 find() 메서드를 이용하여 기본 키(PK)로 단일 엔티티(Menu)를 조회함
     * - 트랜잭션 없이도 단순 조회는 가능함
     *
     * @param menuCode 조회할 메뉴 코드 (PK)
     * @return 조회된 Menu 엔티티 (없으면 null 반환)
     */
    public Menu findMenuByMenuCode(int menuCode) {
        entityManager = EntityManagerGenerator.getInstance();
        return entityManager.find(Menu.class, menuCode);
    }

    /**
     * 2. 새로운 메뉴 등록 후 전체 메뉴 개수 반환
     * <p>
     * - persist()를 통해 newMenu를 영속성 컨텍스트에 등록
     * - 트랜잭션 시작 → 영속화(persist) → 커밋 순으로 처리
     * - 커밋 이후 DB에 실제 반영됨
     *
     * @param newMenu 새로 저장할 Menu 객체
     * @return 저장 이후 전체 메뉴 개수
     */
    public Long saveAndReturnAllCount(Menu newMenu) {
        entityManager = EntityManagerGenerator.getInstance();

        // 트랜잭션 객체 획득 및 시작
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        // 새 엔티티 영속화
        entityManager.persist(newMenu);

        // 커밋 → DB에 실제 반영
        entityTransaction.commit();

        // 등록 이후 전체 메뉴 개수 조회
        return getCount(entityManager);
    }

    /**
     * 현재 DB에 등록된 전체 메뉴 수를 반환
     * <p>
     * - JPQL을 사용한 집계 쿼리
     * - SELECT COUNT(*) 은 JPQL에서 COUNT(m) 또는 COUNT(*) 형태로 사용 가능
     *
     * @param entityManager EntityManager 인스턴스
     * @return 전체 메뉴 개수
     */
    private Long getCount(EntityManager entityManager) {
        // 'Section02Menu'는 @Entity(name = "Section02Menu")에서 지정한 이름
        return entityManager.createQuery("SELECT COUNT(*) FROM Section02Menu", Long.class)
            .getSingleResult();
    }

    /**
     * 3. 메뉴 이름 수정하기
     * <p>
     * - 주어진 menuCode에 해당하는 메뉴를 조회하여 이름을 변경함
     * - 트랜잭션을 시작한 뒤, 엔티티를 수정하고 커밋하여 DB에 반영
     * - JPA는 변경 감지를 통해 자동으로 update SQL을 생성함
     *
     * @param menuCode  수정할 대상 메뉴의 PK
     * @param menuName  변경할 메뉴 이름
     * @return 수정된 Menu 엔티티
     */
    public Menu modifyMenuName(int menuCode, String menuName) {
        entityManager = EntityManagerGenerator.getInstance();

        // 수정 대상 엔티티 조회
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        // 트랜잭션 시작
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // 엔티티의 필드 값 변경 → JPA가 변경 감지하여 update 수행
        foundMenu.setMenuName(menuName);

        // 트랜잭션 커밋 → 변경사항 DB 반영
        transaction.commit();

        return foundMenu;
    }

}
