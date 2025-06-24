package com.ohgiraffers.springdatajpa.menu.controller;

import com.ohgiraffers.springdatajpa.common.Pagenation;
import com.ohgiraffers.springdatajpa.common.PagingButton;
import com.ohgiraffers.springdatajpa.menu.dto.CategoryDTO;
import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.service.MenuService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner.Mode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j // 로그 출력을 위한 Lombok 어노테이션 (log.info 등 사용 가능)
@Controller
@RequestMapping("/menu")
@RequiredArgsConstructor  // final 필드에 대한 생성자를 자동 생성 (의존성 주입 간소화)
public class MenuController {

    // 비즈니스 로직을 처리할 서비스 객체 주입
    private final MenuService menuService;

    /**
     * 단건 메뉴 조회 기능
     *
     * @param menuCode 조회할 메뉴의 고유 번호 (PathVariable로 URL에서 전달받음)
     * @param model    뷰에 전달할 데이터를 저장하는 Spring Model 객체
     * @return "menu/detail" 뷰 반환
     */
    @GetMapping("/{menuCode}")
    public String findMenuByCode(@PathVariable int menuCode, Model model) {

        MenuDTO resultMenu = menuService.findMenuByMenuCode(menuCode);
        model.addAttribute("menu", resultMenu);

        return "menu/detail";
    }

    // 페이징 처리 이전의 전체 메뉴 리스트 조회 (주석 처리된 버전)
    /*
    @GetMapping("/list")
    public String findMenuList(Model model) {
        List<MenuDTO> menuList = menuService.findMenuList();
        model.addAttribute("menuList", menuList);
        return "menu/list";
    }
    */

    /**
     * 페이징 처리된 메뉴 리스트 조회
     *
     * @param model    뷰에 데이터를 전달할 Model 객체
     * @param pageable 페이징 정보를 담고 있는 객체 (page, size 등 자동 매핑)
     * @return "menu/list" 템플릿 이름 반환
     */
    @GetMapping("/list")
    public String findMenuList(Model model, @PageableDefault Pageable pageable) {

        log.info("pageable : {}", pageable);  // 요청된 페이지 정보 로그

        // 메뉴 목록 조회 (Page<MenuDTO> 반환)
        Page<MenuDTO> menuList = menuService.findMenuList(pageable);

        // 조회 결과에 대한 메타데이터 로깅
        log.info("조회한 내용 목록 : {}", menuList.getContent());
        log.info("총 페이지 수 : {}", menuList.getTotalPages());
        log.info("총 메뉴 수 : {}", menuList.getTotalElements());
        log.info("해당 페이지에 표시 될 요소 수 : {}", menuList.getSize());
        log.info("해당 페이지에 실제 요소 수:{}", menuList.getNumberOfElements());
        log.info("첫 페이지 여부 : {}", menuList.isFirst());
        log.info("마지막 페이지 여부 : {}", menuList.isLast());
        log.info("정렬 방식 : {}", menuList.getSort());
        log.info("여러 페이지 중 현재 인덱스 : {}", menuList.getNumber());

        // 페이지 버튼 정보 계산 (시작/끝 페이지 등)
        PagingButton paging = Pagenation.getPagingButtonInfo(menuList);

        // 모델에 페이징 버튼 및 메뉴 리스트 정보 추가
        model.addAttribute("paging", paging);       // 페이지 번호 범위 정보
        model.addAttribute("menuList", menuList);   // Page<MenuDTO> 객체

        return "menu/list";
    }

    @GetMapping("/querymethod")
    public void querymethodPage() {
    }

    @GetMapping("/search")
    public String findByMenuPrice(@RequestParam Integer menuPrice, Model model) {
        List<MenuDTO> menuList = menuService.findByMenuPrice(menuPrice);
        model.addAttribute("menuList", menuList);
        return "menu/searchResult";
    }

    @GetMapping("/regist")
    public void registPage() {
    }

    @GetMapping("/category")
    @ResponseBody
    public List<CategoryDTO> findCategoryList() {
        return menuService.findAllCategory();
    }

    @PostMapping("/regist")
    public String registMenu(@ModelAttribute MenuDTO menuDTO) {
        menuService.registMenu(menuDTO);
        return "redirect:/menu/list";
    }

    @GetMapping("/modify")
    public void modifyPage() {
    }

    @PostMapping("/modify")
    public String modifyMenu(@ModelAttribute MenuDTO menuDTO) {
        menuService.modifyMenu(menuDTO);
        return "redirect:/menu/" + menuDTO.getMenuCode();
    }

    @GetMapping("/delete")
    public void deletePage() {
    }

    @PostMapping("/delete")
    public String deleteMenu(@RequestParam Integer menuCode) {
        menuService.deleteMenu(menuCode);
        return "redirect:/menu/list";
    }

}
