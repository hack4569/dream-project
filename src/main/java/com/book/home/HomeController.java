package com.book.home;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value = "/")
public class HomeController {
    @RequestMapping(value= "/")
    public String index() {
        return "recommend/index";
    }
}
