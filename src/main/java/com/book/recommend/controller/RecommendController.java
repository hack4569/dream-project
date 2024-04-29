package com.book.recommend.controller;

import com.book.exception.UserException;
import com.book.history.service.HistoryService;
import com.book.model.Category;
import com.book.model.Member;
import com.book.recommend.dto.RecommendDto;
import com.book.recommend.service.RecommendService;
import com.book.user.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = {"/recommend", "/"})
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;
    private final HistoryService historyService;

    @Value("${bookRecommend.host}")
    private String hostName;

    @GetMapping(value = "/")
    public String index(
            @Login Member loginMember,
            Model model, Category category) {

        //로그인 유무에 따른 로직 구현
        long memberId = loginMember == null ? 0 : loginMember.getId();

        List<RecommendDto> recommendList = recommendService.getRecommendList(memberId, category);
        model.addAttribute("recommendList", recommendList);

        model.addAttribute("subCid", category.getSubCid());
        model.addAttribute("hostName", hostName);
        model.addAttribute("loginMember", loginMember);
        if (loginMember == null) {
            model.addAttribute("loginHistoryMsg", "로그인하시면 봤던 책정보는 보이지 않습니다.");
        }

        return "recommend/index";
    }

    @GetMapping(value = "/introduce")
    public String index(Model model) {
        return "recommend/introduce";
    }
    @PostMapping(value = "/history/{bookId}")
    public void saveHistory(
            @Login Member loginMember,
            @PathVariable("bookId") long bookId) {

        long memberId = loginMember == null ? 0 : loginMember.getId();

        if (memberId == 0 || bookId == 0) {
            log.debug("memberId = {}", memberId);
            throw new UserException("로그인 아이디 또는 책id가 없습니다.");
        }

        historyService.saveHistory(loginMember, bookId);
    }
}
