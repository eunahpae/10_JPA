package com.ohgiraffers.springdatajpa.common;

import org.springframework.data.domain.Page;

/**
 * Pagenation 클래스는 Spring Data JPA의 Page 객체를 기반으로
 * 페이징 버튼에 필요한 시작 페이지, 끝 페이지, 현재 페이지 정보를 계산해주는 유틸리티 클래스이다.
 */
public class Pagenation {

    /**
     * Page 객체를 기반으로 페이징 버튼 정보(PagingButton DTO)를 생성하여 반환한다.
     *
     * @param page 조회 결과가 담긴 Page 객체
     * @return 현재 페이지, 시작 페이지, 끝 페이지 정보를 담은 PagingButton 객체
     */
    public static PagingButton getPagingButtonInfo(Page page) {

        // Page.getNumber()는 0부터 시작하므로 사용자에게 보여주기 위해 +1
        int currentPage = page.getNumber() + 1;

        // 한 번에 보여줄 페이지 버튼 개수 (예: << 1 2 3 4 5 6 7 8 9 10 >> 형태)
        int defaultButtonCount = 10;

        /**
         * 시작 페이지 계산 공식:
         *  - 현재 페이지를 버튼 묶음 단위로 나눈 후, 해당 블록의 첫 페이지 계산
         *  - 예: 현재 13페이지일 때 → 11부터 시작 (11~20)
         */
        int startPage
            = (int) (Math.ceil((double) currentPage / defaultButtonCount) - 1)
            * defaultButtonCount + 1;

        // 끝 페이지는 시작 페이지 + (버튼 개수 - 1)
        int endPage = startPage + defaultButtonCount - 1;

        // 전체 페이지 수보다 큰 endPage는 제한
        if (page.getTotalPages() < endPage)
            endPage = page.getTotalPages();

        // 전체 페이지가 0일 경우 endPage도 최소 startPage로 맞춤
        if (page.getTotalPages() == 0 && endPage == 0)
            endPage = startPage;

        // 계산된 정보로 PagingButton DTO 반환
        return new PagingButton(currentPage, startPage, endPage);
    }
}
