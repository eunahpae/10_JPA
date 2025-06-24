package com.ohgiraffers.springdatajpa.menu.service;

import com.ohgiraffers.springdatajpa.menu.dto.CategoryDTO;
import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.entity.Category;
import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import com.ohgiraffers.springdatajpa.menu.repository.CategoryRepository;
import com.ohgiraffers.springdatajpa.menu.repository.MenuRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * MenuService 클래스는 메뉴와 관련된 비즈니스 로직을 담당하는 서비스 계층이다. 컨트롤러로부터 요청을 받아 Repository를 통해 데이터를 조회하고,
 * ModelMapper를 이용하여 엔티티를 DTO로 변환하여 반환한다.
 */
@Service  // 해당 클래스가 서비스 컴포넌트임을 명시하며, 스프링 빈으로 등록된다.
@RequiredArgsConstructor  // final 필드를 대상으로 생성자를 자동 생성하여 의존성 주입을 간결하게 해준다.
public class MenuService {

    // 메뉴 데이터를 조회/저장/삭제하는 Repository. Spring Data JPA가 구현체를 자동으로 생성한다.
    private final MenuRepository menuRepository;

    // 엔티티와 DTO 간 매핑을 자동화하기 위한 ModelMapper 빈
    private final ModelMapper modelMapper;

    private final CategoryRepository categoryRepository;

    /**
     * 주어진 메뉴 코드(menuCode)를 이용해 메뉴 정보를 조회한 후, 해당 엔티티를 MenuDTO로 변환하여 반환한다.
     * <p>
     * - 메뉴가 존재하지 않을 경우 IllegalArgumentException 예외를 발생시킨다. - ModelMapper를 사용해 엔티티의 필드를 DTO로 매핑한다.
     *
     * @param menuCode 조회할 메뉴의 고유 코드(PK)
     * @return 변환된 MenuDTO 객체
     * @throws IllegalArgumentException 해당 ID의 메뉴가 존재하지 않을 경우 발생
     */
    public MenuDTO findMenuByMenuCode(int menuCode) {
        // 메뉴 엔티티 조회 (존재하지 않으면 예외 발생)
        Menu foundMenu = menuRepository.findById(menuCode)
            .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));

        // ModelMapper를 이용해 Menu 엔티티를 MenuDTO로 변환하여 반환
        return modelMapper.map(foundMenu, MenuDTO.class);
    }


    /**
     * findAll : sort 모든 메뉴 목록을 메뉴 코드(menuCode) 기준 내림차순 정렬하여 조회한다. 조회된 Menu 엔티티 리스트는 스트림을 이용해
     * ModelMapper로 각각 MenuDTO 리스트로 변환된다.
     *
     * @return 메뉴 정보가 담긴 MenuDTO 리스트
     */
    public List<MenuDTO> findMenuList() {
        // menuCode 기준 내림차순으로 정렬하여 전체 메뉴 조회
        List<Menu> menuList = menuRepository.findAll(Sort.by("menuCode").descending());

        // menuList(엔티티 객체 리스트)를 스트림(Stream)으로 변환한다.
        // 스트림은 컬렉션 요소를 함수형 스타일로 처리할 수 있는 연속된 데이터 흐름이다.
        return menuList.stream()
            // 각 엔티티(menu)를 ModelMapper를 사용해 MenuDTO 타입으로 변환(map)한다.
            // map() 메서드는 스트림의 각 요소에 함수를 적용하여 새로운 요소로 변환하는 역할을 한다.
            .map(menu -> modelMapper.map(menu, MenuDTO.class))
            // 변환된 DTO 객체들을 다시 리스트로 수집하여 최종 결과로 반환한다.
            .toList();  // Java 16 이상: stream().collect(Collectors.toList()) 대신 사용 가능
    }

    /**
     * findAll : Pageable
     * <p>
     * Pageable 객체를 이용하여 요청된 페이지 정보에 맞게 메뉴 목록을 조회하고, 조회된 Page<Menu>를 Page<MenuDTO>로 변환하여 반환한다.
     * <p>
     * - 클라이언트에서 전달된 페이지 번호가 0 이하일 경우, 0페이지로 고정하여 처리한다. - menuCode 기준 내림차순 정렬을 적용하여 데이터를 조회한다. - 반환
     * 타입은 Page<MenuDTO>로, 페이징 정보와 DTO 리스트를 함께 제공한다.
     *
     * @param pageable 클라이언트로부터 전달받은 페이지 요청 정보 (page 번호, size 등)
     * @return Page<MenuDTO> 객체 (내용 + 전체 페이지 수, 현재 페이지 번호 등 메타데이터 포함)
     */
    public Page<MenuDTO> findMenuList(Pageable pageable) {

        // 페이지 번호가 0 이하로 들어올 경우 0페이지로 고정 (Spring Data는 0부터 시작)
        pageable = PageRequest.of(
            pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
            pageable.getPageSize(),
            Sort.by("menuCode").descending()  // menuCode 기준 내림차순 정렬 적용
        );

        // menuRepository에서 menuCode 내림차순 기준으로 Page<Menu> 조회
        Page<Menu> menuList = menuRepository.findAll(pageable);

        // Page<Menu>를 Page<MenuDTO>로 변환
        // Page.map(Function<T, R>)은 각 엔티티 요소에 매핑 함수를 적용하여 새 Page 객체로 변환한다.
        // 내부적으로는 스트림을 사용하므로 간결하면서도 성능적으로 효율적이다.
        return menuList.map(menu -> modelMapper.map(menu, MenuDTO.class));
    }

    /**
     * Query Method: 메뉴 가격이 특정 값보다 높은 메뉴 목록을 조회한다.
     * <p>
     * - Spring Data JPA의 쿼리 메서드 명명 규칙을 이용해 repository에서 자동 쿼리 생성 - 정렬 조건은 Sort 객체를 통해 동적으로 전달
     * (menuPrice 기준 내림차순) - 조회된 엔티티(Menu)를 DTO(MenuDTO)로 변환하여 반환
     *
     * @param menuPrice 기준이 되는 메뉴 가격
     * @return 가격 조건을 만족하는 MenuDTO 리스트
     */
    public List<MenuDTO> findByMenuPrice(Integer menuPrice) {

        // List<Menu> menuList = menuRepository.findByMenuPriceGreaterThan(menuPrice);
        // List<Menu> menuList = menuRepository.findByMenuPriceGreaterThanOrderByMenuPrice(menuPrice);

        // 정렬 조건: menuPrice 기준 내림차순
        List<Menu> menuList = menuRepository.findByMenuPriceGreaterThan(
            menuPrice,
            Sort.by("menuPrice").descending()
        );

        return menuList.stream()
            .map(menu -> modelMapper.map(menu, MenuDTO.class))
            .toList();
    }

    /* JPQL or Native Query */
    public List<CategoryDTO> findAllCategory() {
        List<Category> categoryList = categoryRepository.findAllCategory();
        return categoryList.stream().map(category -> modelMapper.map(category, CategoryDTO.class))
            .toList();
    }

    /**
     * registMenu: 신규 메뉴 등록
     * <p>
     * - 전달받은 MenuDTO 객체를 ModelMapper를 사용해 Menu 엔티티로 변환한 뒤 저장한다. - @Transactional 어노테이션으로 트랜잭션을
     * 보장한다.
     *
     * @param menuDTO 신규 등록할 메뉴 정보
     */
    @Transactional
    public void registMenu(MenuDTO menuDTO) {
        menuRepository.save(modelMapper.map(menuDTO, Menu.class));
    }

    /**
     * 메뉴 이름 수정 - Setter 사용 지양, 기능에 맞는 메서드를 엔티티에 정의해 사용
     *
     * @param menuDTO 수정할 메뉴 정보
     * @throws IllegalArgumentException 메뉴가 존재하지 않을 경우
     */
    @Transactional
    public void modifyMenu(MenuDTO menuDTO) {
        Menu foundMenu = menuRepository.findById(menuDTO.getMenuCode())
            .orElseThrow(IllegalArgumentException::new);

        foundMenu.modifyMenuName(menuDTO.getMenuName());
    }

    @Transactional
    public void deleteMenu(Integer menuCode) {
        menuRepository.deleteById(menuCode);
    }
}
