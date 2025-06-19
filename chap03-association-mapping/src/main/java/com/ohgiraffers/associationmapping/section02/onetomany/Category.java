package com.ohgiraffers.associationmapping.section02.onetomany;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity(name = "category_and_menu")
@Table(name = "tbl_category")
public class Category {

    @Id
    private int categoryCode;
    private String categoryName;
    private Integer refCategoryCode;

    /* @OneToMany의 fetch 전략
     * - 기본값: LAZY(지연 로딩) - 컬렉션 사용 시점에 쿼리 실행 (권장)
     * - EAGER: 즉시 로딩 - 엔티티 조회 시 컬렉션도 즉시 로딩 (N+1 문제 발생 가능)
     *
     * 사용법: @OneToMany(fetch = FetchType.LAZY) // 생략 가능 */

    /* 일대다(@OneToMany) 연관관계
     * - 하나(1)의 카테고리가 여러(N)개의 메뉴를 포함하는 관계
     * - 외래키는 Menu 테이블의 categoryCode 컬럼에 존재
     * - 연관관계 주인은 외래키가 있는 Menu 쪽 */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "categoryCode") // 외래키 컬럼명 지정
    private List<Menu> menuList;
    public Category() {
    }

    public Category(int categoryCode, String categoryName, Integer refCategoryCode,
        List<Menu> menuList) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.refCategoryCode = refCategoryCode;
        this.menuList = menuList;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getRefCategoryCode() {
        return refCategoryCode;
    }

    public void setRefCategoryCode(Integer refCategoryCode) {
        this.refCategoryCode = refCategoryCode;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(
        List<Menu> menuList) {
        this.menuList = menuList;
    }

    @Override
    public String toString() {
        return "Category{" +
            "categoryCode=" + categoryCode +
            ", categoryName='" + categoryName + '\'' +
            ", refCategoryCode=" + refCategoryCode +
            ", menuList=" + menuList +
            '}';
    }
}
