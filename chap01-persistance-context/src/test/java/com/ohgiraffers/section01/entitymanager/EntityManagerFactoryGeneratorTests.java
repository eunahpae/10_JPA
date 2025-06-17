package com.ohgiraffers.section01.entitymanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EntityManagerFactoryGeneratorTests {

    @Test
    @DisplayName("엔티티 매니저 팩토리 생성 확인")
    void testGenerateEntityManagerFactory() {

        // comment. 보통 given, when, then 3-steps 으로 테스트 코드 작성
        // 1. given: 테스트에 필요한 사전 조건 설정
        // 이 테스트의 경우 별도의 입력값이 필요하지 않으므로 생략

        // 2. when: 테스트 대상 메서드 실행
        EntityManagerFactory factory = EntityManagerFactoryGenerator.getInstance();

        // 3. then: 실행 결과에 대한 검증 수행
        // EntityManagerFactory 객체가 null이 아님을 확인하여 정상 생성 여부 테스트
        assertNotNull(factory);

    }

    @Test
    @DisplayName("엔티티 매니저 싱글톤 인스턴스 확인")
    void testIsEntityManagerFactorySingletonInstance() {

        // when
        EntityManagerFactory factory1 = EntityManagerFactoryGenerator.getInstance();
        EntityManagerFactory factory2 = EntityManagerFactoryGenerator.getInstance();

        // then
        assertEquals(factory1, factory2);  // 싱글톤 여부 검증

        // 참고용 출력
        System.out.println("factory1: " + factory1);
        System.out.println("factory2: " + factory2);
        System.out.println("동일 인스턴스 여부: " + (factory1 == factory2));
    }

    

}
