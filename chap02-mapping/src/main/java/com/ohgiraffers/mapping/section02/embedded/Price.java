package com.ohgiraffers.mapping.section02.embedded;

import jakarta.persistence.*;

/**
 * 값 타입 클래스 정의
 * - 이 클래스는 다른 엔티티에 내장될 수 있도록 설계된 '임베디드 타입'이다.
 * - 엔티티가 아닌 일반 객체이지만, JPA가 필드들을 컬럼으로 매핑한다.
 * - 해당 클래스를 사용하는 모든 엔티티에 동일한 방식으로 포함될 수 있다.
 */
@Embeddable
public class Price {

    @Column(name = "regular_price")
    private int regularPrice;
    @Column(name = "discount_rate")
    private double discountRate;
    @Column(name = "sell_price")
    private int sellPrice;

    protected Price() {
    }

    public Price(int regularPrice, double discountRate) {
        validateNagativePrice(regularPrice);
        validateNegativeDiscountRate(discountRate);
        this.regularPrice = regularPrice;
        this.discountRate = discountRate;
        this.sellPrice = calcSellPrice(regularPrice, discountRate);
    }

    private void validateNagativePrice(int regularPrice) {
        if (regularPrice < 0) {
            throw new IllegalArgumentException("가격은 음수일 수 없습니다.");
        }
    }

    private void validateNegativeDiscountRate(double discountRate) {
        if (discountRate < 0) {
            throw new IllegalArgumentException("할인율은 음수일 수 없습니다.");
        }
    }

    private int calcSellPrice(int regularPrice, double discountRate) {
        return (int) (regularPrice - (regularPrice * discountRate));
    }

    @Override
    public String toString() {
        return "Price{" +
            "regularPrice=" + regularPrice +
            ", discountRate=" + discountRate +
            ", sellPrice=" + sellPrice +
            '}';
    }
}