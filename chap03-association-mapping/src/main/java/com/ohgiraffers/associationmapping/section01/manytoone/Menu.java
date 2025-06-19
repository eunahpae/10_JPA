package com.ohgiraffers.associationmapping.section01.manytoone;

import jakarta.persistence.*;

@Entity(name = "menu_and_category")
@Table(name = "tbl_menu")
public class Menu {

    @Id
    private int menuCode;
    private String menuName;
    private int menuPrice;

    /* 다대일(N:1) 관계 설정
     * - 다수(N)의 메뉴가 하나(1)의 카테고리에 속하는 관계
     * - 외래키는 Menu 테이블의 categoryCode 컬럼에 생성됨
     * - CascadeType.PERSIST: Menu 저장 시 연관된 Category도 함께 저장 */

    /* @ManyToOne의 fetch 전략
     * - 기본값: EAGER(즉시 로딩) - 엔티티 조회 시 연관 엔티티도 즉시 로딩
     * - 권장값: LAZY(지연 로딩) - 실제 사용 시점에 쿼리 실행, N+1 문제 방지
     *
     * 사용법: @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY) */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "categoryCode") // 외래키 컬럼명 지정
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
