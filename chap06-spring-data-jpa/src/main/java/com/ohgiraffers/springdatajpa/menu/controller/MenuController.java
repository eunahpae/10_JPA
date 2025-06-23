package com.ohgiraffers.springdatajpa.menu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @GetMapping("/{menuCode}")
    public String findMenuByCode(int menuCode) {
        return "/{menuCode}";
    }


}
