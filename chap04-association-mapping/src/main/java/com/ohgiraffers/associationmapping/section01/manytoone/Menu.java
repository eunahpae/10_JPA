package com.ohgiraffers.associationmapping.section01.manytoone;

import jakarta.persistence.*;

@Entity(name = "menu_and_category")
@Table(name = "tbl_menu")
public class Menu {

    @Id
    private int menuCode;
    private String menuName;
    private int menuPrice;

    // 다대일(N:1) 관계 설정
    // - 이 엔티티는 다수(N)의 입장에서 연관된 단일(1) Category 엔티티를 참조한다.
    // - 예: 하나의 메뉴(Menu)는 하나의 카테고리(Category)에 속한다.
    // - CascadeType.PERSIST: 연관된 Category가 영속 상태가 아니면, Menu 저장 시 함께 저장됨 (전파 저장)
    @ManyToOne(cascade = CascadeType.PERSIST)
    // 외래 키 매핑
    // - 이 엔티티의 테이블에 생성될 외래 키 컬럼명을 "categoryCode"로 지정
    // - 기본적으로 참조 대상 엔티티(Category)의 PK를 외래 키로 사용
    // - name 속성은 외래 키 컬럼 이름을 명시적으로 지정한다.
    @JoinColumn(name = "categoryCode")
    private Category category;
    private String orderableStatus;

    public Menu() {
    }

    public Menu(int menuCode, String menuName, int menuPrice, Category category,
        String orderableStatus) {
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.category = category;
        this.orderableStatus = orderableStatus;
    }

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getOrderableStatus() {
        return orderableStatus;
    }

    public void setOrderableStatus(String orderableStatus) {
        this.orderableStatus = orderableStatus;
    }

    @Override
    public String toString() {
        return "Menu{" +
            "menuCode=" + menuCode +
            ", menuName='" + menuName + '\'' +
            ", menuPrice=" + menuPrice +
            ", category=" + category +
            ", orderableStatus='" + orderableStatus + '\'' +
            '}';
    }
}
