package com.book.recommend.controller;

import com.book.category.dto.CategoryDto;
import com.book.exception.UserException;
import com.book.history.service.HistoryService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Controller
@RequestMapping(value = {"/recommend", "/"})
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;
    private final HistoryService historyService;

    @GetMapping(value = "/")
    public String index(
            @Login Member loginMember,
            Model model, CategoryDto categoryDto) {

        //로그인 유무에 따른 로직 구현
        long memberId = loginMember == null ? 0 : loginMember.getId();

        long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
        List<RecommendDto> recommendList = new ArrayList<>();
        CompletableFuture<List<RecommendDto>> recommendList1 = CompletableFuture.supplyAsync(() -> recommendService.getRecommendList(memberId, categoryDto, 1));
        CompletableFuture<List<RecommendDto>> recommendList2 = CompletableFuture.supplyAsync(() -> recommendService.getRecommendList(memberId, categoryDto, 2));
        CompletableFuture<List<RecommendDto>> recommendList3 = CompletableFuture.supplyAsync(() -> recommendService.getRecommendList(memberId, categoryDto, 3));
        CompletableFuture<List<RecommendDto>> recommendList4 = CompletableFuture.supplyAsync(() -> recommendService.getRecommendList(memberId, categoryDto, 4));
        CompletableFuture<List<RecommendDto>> recommendList5 = CompletableFuture.supplyAsync(() -> recommendService.getRecommendList(memberId, categoryDto, 0));
        CompletableFuture<Void> allRecommendList = CompletableFuture.allOf(recommendList1, recommendList2, recommendList3, recommendList4, recommendList5);
        allRecommendList.thenRun(() -> {
            try {
                recommendList.addAll(recommendList1.get());
                recommendList.addAll(recommendList2.get());
                recommendList.addAll(recommendList3.get());
                recommendList.addAll(recommendList4.get());
                recommendList.addAll(recommendList5.get());
            } catch (Exception e) {
                log.error("error : {}", e);
            }
        }).join();

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
        System.out.println("시간차이(m) : "+secDiffTime);
        model.addAttribute("recommendList", recommendList);
        model.addAttribute("subCid", categoryDto.getSubCid());
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
