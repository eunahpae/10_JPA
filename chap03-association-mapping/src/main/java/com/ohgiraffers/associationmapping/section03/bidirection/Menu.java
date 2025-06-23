package com.ohgiraffers.associationmapping.section03.bidirection;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "bidirection_menu")
@Table(name = "tbl_menu")
public class Menu {

    @Id
    private int menuCode;
    private String menuName;
    private int menuPrice;

    /* ✅ 양방향 연관관계 설정 */
    // 연관관계의 주인(진짜 연관관계)
    // - Menu(자식 엔티티)가 외래 키(categoryCode)를 소유하고 있으므로 연관관계의 주인이다.
    // - 연관관계의 주인은 DB에 외래 키 값을 저장하거나 변경할 수 있다.
    // - mappedBy 속성이 없으므로 이 필드가 연관관계의 주인임을 의미한다.
    @ManyToOne
    @JoinColumn(name = "categoryCode")  // 외래 키 컬럼 이름 명시 (tbl_menu.categoryCode)
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
