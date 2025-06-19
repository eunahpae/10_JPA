package com.ohgiraffers.mapping.section01.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    // EntityManager는 JPA의 핵심 인터페이스로, 엔티티의 CRUD 및 쿼리 실행 등을 담당한다.
    // @PersistenceContext는 스프링이 EntityManager를 주입하도록 해주는 어노테이션이다.
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 새로운 회원 엔티티를 영속성 컨텍스트에 저장한다.
     * persist() 호출 시 해당 엔티티는 영속 상태가 되며,
     * 트랜잭션 커밋 시 DB에 insert 쿼리가 실행된다.
     *
     * @param member 저장할 회원 엔티티
     */
    public void save(Member member) {
        entityManager.persist(member);
    }

    /**
     * PK가 아닌 '회원 ID' 를 기반으로 회원 이름을 조회
     *
     * JPQL을 사용하여 엔티티 기반으로 질의하며, 실제 테이블명이 아닌 엔티티명을 사용해야 한다.
     * 반드시 별칭(alias)을 지정해야 하며, 문자열 연결 방식은 SQL Injection 위험이 있으므로
     * 실제 운영 환경에서는 파라미터 바인딩을 사용하는 것이 바람직하다.
     *
     * @param memberId 조회할 회원의 ID
     * @return 해당 회원의 이름
     */
    public String findNameById(String memberId) {

        // Entity 이름은 클래스 이름(Member)이 아니라, @Entity(name="...") 지정값이 있을 경우 그것을 사용해야 한다.
        String jpql =
            "SELECT m.memberName FROM entityMember m WHERE m.memberId = '" + memberId + "'";

        // comment. 위 예시는 테스트용 예시이며, 실무에서는 반드시 파라미터 바인딩을 사용할 것.
        // "SELECT m.memberName FROM entityMember m WHERE m.memberId = :memberId";

        // createQuery는 타입 불명확성을 피하기 위해 타입 지정.
        return entityManager.createQuery(jpql, String.class).getSingleResult();

        // comment. setParameter()를 사용해 SQL Injection 방지 및 가독성 향상.
        // return entityManager.createQuery(jpql, String.class).setParameter("memberId", memberId).getSingleResult();
    }

}
