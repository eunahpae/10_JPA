package com.ohgiraffers.nativequery.section01.simple;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.Id;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.SqlResultSetMappings;
import jakarta.persistence.Table;

@SqlResultSetMappings(
    value =
        {
            /* 자동 엔티티 매핑 : @Column으로 매핑 설정이 되어 있는 경우 사용 */
            @SqlResultSetMapping(
                // 결과 매핑 이름
                name = "categoryCountAutoMapping",
                // @EntityResult를 사용해서 엔티티를 결과로 매핑
                entities = {@EntityResult(entityClass = Category.class)},
                // @ColumnResult를 사용해서 컬럼을 결과로 매핑
                columns = {@ColumnResult(name = "menu_count")}
            ),
            /* 수동 엔티티 매핑 : @Column으로 매핑 설정이 되어 있지 않은 경우 사용 */
            @SqlResultSetMapping(
                name = "categoryCountManualMapping",
                entities = {
                    @EntityResult(
                        entityClass = Category.class,
                        fields = {
                            @FieldResult(name = "categoryCode",
                                column = "category_code"),
                            @FieldResult(name = "categoryName",
                                column = "category_name"),
                            @FieldResult(name = "refCategoryCode",
                                column = "ref_category_code")
                        })
                },
                columns = {@ColumnResult(name = "menu_count")}
            )
        }
)
@Entity(name = "Section01Category")
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
