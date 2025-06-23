package com.ohgiraffers.nativequery.section02.namedquery;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;

@SqlResultSetMapping(name = "categoryCountAutoMapping2",
    entities = {@EntityResult(entityClass = Category.class)},
    columns = {@ColumnResult(name = "menu_count")}
)
@NamedNativeQueries(
    value = {
        @NamedNativeQuery(
            name = "Category.menuCountOfCategory",
            query = "SELECT a.category_code, a.category_name, a.ref_category_code," +
                " COALESCE(v.menu_count, 0) menu_count FROM tbl_category a" +
                " LEFT JOIN (SELECT COUNT(*) AS menu_count, b.category_code FROM tbl_menu b" +
                " GROUP BY b.category_code) v ON (a.category_code = v.category_code)" +
                " ORDER BY 1",
            resultSetMapping = "categoryCountAutoMapping2"
        )
    }
)

@Entity(name = "Section02Category")
@Table(name = "tbl_category")
public class Category {

    @Id
    private int categoryCode;
    private String categoryName;
    private Integer refCategoryCode;

    @Override
    public String toString() {
        return "Category{" +
            "categoryCode=" + categoryCode +
            ", categoryName='" + categoryName + '\'' +
            ", refCategoryCode=" + refCategoryCode +
            '}';
    }
}
