package com.ohgiraffers.associationmapping.section03.bidirection;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "bidirection_category")
@Table(name = "tbl_category")
public class Category {

    @Id
    private int categoryCode;
    private String categoryName;
    private Integer refCategoryCode;

    /* ✅ 양방향 연관관계 설정 */
    // 연관관계의 비주인(가짜 연관관계)
    // - mappedBy = "category"는 Menu 엔티티의 'category' 필드가 연관관계의 주인임을 나타냄
    // - 이 필드는 외래 키를 관리하지 않기 때문에 DB에 직접적인 변경 사항을 반영하지 않는다.
    // - 단순히 '카테고리에 속한 메뉴들'을 조회할 수 있는 읽기 전용 관계이다.
    @OneToMany(mappedBy = "category")
    private List<Menu> menuList = new ArrayList<>();

    protected Category() {
    }

    public Category(
        int categoryCode, String categoryName, Integer refCategoryCode
    ) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.refCategoryCode = refCategoryCode;
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

    @Override
    public String toString() {
        return "Category{" +
            "categoryCode=" + categoryCode +
            ", categoryName='" + categoryName + '\'' +
            ", refCategoryCode=" + refCategoryCode +
            '}';
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

}