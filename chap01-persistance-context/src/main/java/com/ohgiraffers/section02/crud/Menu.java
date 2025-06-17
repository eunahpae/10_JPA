package com.ohgiraffers.section02.crud;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Menu 엔티티 클래스
 * - tbl_menu 테이블과 매핑
 * - JPA를 활용한 CRUD 대상 객체
 */
@Entity(name = "Section02Menu") // JPA 엔티티 이름 지정 (JPQL 등에서 사용됨)
@Table(name = "tbl_menu")       // 매핑될 실제 테이블 이름 지정
public class Menu {

    @Id // 기본 키(primary key) 지정
    @Column(name = "menu_code") // 실제 테이블의 컬럼 이름과 매핑
    private int menuCode;

    @Column(name = "menu_name") // 메뉴 이름 컬럼과 매핑
    private String menuName;

    @Column(name = "menu_price") // 메뉴 가격 컬럼과 매핑
    private int menuPrice;

    @Column(name = "category_code") // 카테고리 코드 컬럼과 매핑
    private int categoryCode;

    @Column(name = "orderable_status") // 주문 가능 상태 컬럼과 매핑
    private char orderableStatus;

    /**
     * 기본 생성자
     * - JPA 스펙에 따라 반드시 필요
     * - protected로 설정하여 외부에서 직접 호출은 제한
     */
    protected Menu() {}

    // 전체필드 생성자도 테스트를 위해 생성
    public Menu(int menuCode, String menuName, int menuPrice, int categoryCode,
        char orderableStatus) {
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.categoryCode = categoryCode;
        this.orderableStatus = orderableStatus;
    }

    // getter/setter 생성을 권장하지 않지만, 테스트를 위해 생성
    public int getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(int menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public char getOrderableStatus() {
        return orderableStatus;
    }

    public void setOrderableStatus(char orderableStatus) {
        this.orderableStatus = orderableStatus;
    }

    @Override
    public String toString() {
        return "Menu{" +
            "menuCode=" + menuCode +
            ", menuName='" + menuName + '\'' +
            ", menuPrice=" + menuPrice +
            ", categoryCode=" + categoryCode +
            ", orderableStatus=" + orderableStatus +
            '}';
    }
}
