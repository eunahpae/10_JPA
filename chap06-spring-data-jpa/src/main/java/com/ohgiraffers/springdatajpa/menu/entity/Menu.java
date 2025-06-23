package com.ohgiraffers.springdatajpa.menu.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_menu")
@Getter
// Lombok 어노테이션: 모든 필드에 대해 Getter 메서드를 자동 생성한다.
// → 객체의 상태를 외부에서 확인할 수는 있지만, 직접 변경은 불가능하도록 하여 불변성을 유지한다.

/* ❌ Setter는 명시적으로 정의하지 않는다.
 * - Setter를 열어두면 객체 상태를 언제든 변경할 수 있어 캡슐화가 깨짐
 * - 디버깅/유지보수 어려움 + 객체 일관성 저하
 * ✅ 필요한 경우, 의미를 갖는 변경 메서드를 별도로 정의해 사용하는 것이 바람직하다. */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
// Lombok 어노테이션: 파라미터가 없는 기본 생성자를 생성한다.
// - JPA에서는 리플렉션 및 프록시 생성을 위해 기본 생성자가 필수
// - access = PROTECTED로 설정하여 외부에서 무분별한 인스턴스 생성을 제한
// ⚠ @AllArgsConstructor는 객체 일관성을 깨뜨릴 수 있어 실무에서는 지양
// → 대신 정적 팩토리 메서드 또는 @Builder 패턴 사용을 권장

/* @ToString: 사용 가능하지만, 연관관계 매핑 필드가 있을 경우 StackOverflowError의 원인이 될 수 있음.
 * → 순환 참조 방지를 위해 연관 필드는 제외하거나, DTO에서 사용 권장 */
public class Menu {

    @Id
    private int menuCode;
    private String menuName;
    private int menuPrice;
    private int categoryCode;
    private char orderableStatus;

}
