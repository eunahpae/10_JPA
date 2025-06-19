package com.ohgiraffers.mapping.section01.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * 회원 정보를 나타내는 JPA Entity 클래스
 */
@Entity(name = "entityMember") // JPA Entity로 등록, 엔티티명을 "entityMember"로 지정
@Table(name = "tbl_member")    // 실제 데이터베이스 테이블명을 "tbl_member"로 매핑
@Access(AccessType.FIELD)
// JPA가 엔티티 필드에 접근할 때 사용하는 방식 지정 (FIELD: 필드를 기준으로 매핑)
// 클래스 단위로 지정되며, 모든 필드에 일괄 적용된다.
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정 (MySQL 등에서 자동 증가)
    @Column(name = "member_no") // 데이터베이스 컬럼명 매핑
    private int memberNo;

    @Access(AccessType.FIELD)
    @Column(
        name = "member_id",
        unique = true,                // UNIQUE 제약조건 설정 (중복값 불허)
        nullable = false,             // NOT NULL 제약조건 설정 (필수값)
        columnDefinition = "varchar(10)" // 직접 SQL 타입 정의
    )
    private String memberId;

    @Column(name = "member_pwd", nullable = false)
    private String memberPwd;

    // 이 필드는 필드 단위가 아닌 "프로퍼티(즉, getter/setter 메서드)"를 통해 접근하도록 설정한다.
    // getter 메서드 위에 매핑 어노테이션이 있어야 정상 작동한다.
    // @Access(AccessType.PROPERTY)
    @Column(name = "member_name")
    private String memberName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address", length = 900) // VARCHAR(900)으로 컬럼 길이 제한 설정
    private String address;

    @Column(name = "enroll_date")
    private LocalDateTime enrollDate;

    @Column(name = "member_role")
    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장 (vs ORDINAL: 숫자로 저장)
    // EnumType.STRING: 실제 enum 상수명을 문자열로 DB에 저장 (예: "ADMIN", "USER")
    // EnumType.ORDINAL: enum의 순서를 숫자로 저장 (예: 0, 1, 2...)
    // STRING 방식이 더 안전함 - enum 순서가 바뀌어도 데이터 무결성 유지
    private MemberRole memberRole;

    @Column(
        name = "status",
        columnDefinition = "char(1) default 'Y'" // CHAR(1) 타입으로 기본값 'Y' 설정
    )
    private String status;

    protected Member() {
    }

    /* memberNo는 AUTO_INCREMENT이므로 제외 */
    public Member(String memberId, String memberPwd, String memberName, String phone,
        String address, LocalDateTime enrollDate, MemberRole memberRole, String status) {
        this.memberId = memberId;
        this.memberPwd = memberPwd;
        this.memberName = memberName;
        this.phone = phone;
        this.address = address;
        this.enrollDate = enrollDate;
        this.memberRole = memberRole;
        this.status = status;
    }

    @Access(AccessType.PROPERTY)
    public String getMemberName() {
        System.out.println("getMemberName 메소드를 통한 Access 확인");
        return memberName + "님";
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
