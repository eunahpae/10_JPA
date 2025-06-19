package com.ohgiraffers.mapping.section01.entity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 등록 비즈니스 로직을 담당하는 서비스 클래스
 */
@Service
public class MemberRegistService {

    // final 키워드 추가 (불변성 보장)
    private final MemberRepository memberRepository;

    /**
     * 생성자 기반 의존성 주입
     * Spring 4.3 이후부터는 단일 생성자의 경우 @Autowired 생략 가능
     *
     * @param memberRepository 회원 데이터 접근을 위한 레포지토리
     */
    public MemberRegistService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 등록 메서드
     *
     * Entity가 아닌 DTO를 매개변수로 사용하는 이유:
     * 1. 계층 간 분리: 컨트롤러 계층과 서비스 계층 간의 데이터 전달 객체
     * 2. 데이터 검증: DTO 에서 입력값 검증 처리 가능 (@Valid 등)
     * 3. 보안: 엔티티의 모든 필드가 외부에 노출되는 것을 방지
     * 4. 유연성: 클라이언트 요구사항에 맞는 데이터 구조 제공
     * 5. 엔티티 캡슐화: 엔티티의 내부 구조 변경이 외부 API에 영향을 주지 않음
     * 6. 불필요한 데이터 전송 방지: 필요한 데이터만 선별적으로 전달
     *
     * @param newMember 등록할 회원 정보를 담은 DTO
     */
    @Transactional
    // 데이터베이스 트랜잭션 관리 (ACID 속성 보장)
    // 메서드 실행 중 예외 발생 시 자동 롤백
    // save() 작업이 완전히 성공해야 커밋
    public void registMember(MemberRegistDTO newMember) {

        // DTO → Entity로 변환 (데이터 매핑)
        // 이 과정에서 비즈니스 로직 검증이나 데이터 변환 로직 추가 가능
        Member member = new Member(
            newMember.getMemberId(),
            newMember.getMemberPwd(),
            newMember.getMemberName(),
            newMember.getPhone(),
            newMember.getAddress(),
            newMember.getEnrollDate(),
            newMember.getMemberRole(),
            newMember.getStatus()
        );

        // JPA Repository를 통한 데이터 저장
        // save() 메서드는 영속성 컨텍스트에 엔티티를 저장하고
        // 트랜잭션 커밋 시점에 실제 데이터베이스에 INSERT 쿼리 실행
        memberRepository.save(member);
    }

    @Transactional
    public String registMemberAndFindName(MemberRegistDTO newMember) {
        registMember(newMember);
        return memberRepository.findNameById(newMember.getMemberId());
    }

}
