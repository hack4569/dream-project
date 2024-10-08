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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        List<RecommendDto> recommendList = new ArrayList<>();
        CompletableFuture<List<RecommendDto>> recommendList1 = CompletableFuture.supplyAsync(() -> recommendService.getRecommendList(loginMember, categoryDto, 1));
        CompletableFuture<List<RecommendDto>> recommendList2 = CompletableFuture.supplyAsync(() -> recommendService.getRecommendList(loginMember, categoryDto, 2));
        CompletableFuture<List<RecommendDto>> recommendList5 = CompletableFuture.supplyAsync(() -> recommendService.getRecommendList(loginMember, categoryDto, 0));
        CompletableFuture<Void> allRecommendList = CompletableFuture.allOf(recommendList1, recommendList2, recommendList5);
        allRecommendList.thenRun(() -> {
            try {
                recommendList.addAll(recommendList1.get());
                recommendList.addAll(recommendList2.get());
                recommendList.addAll(recommendList5.get());
            } catch (Exception e) {
                log.error("error : {}", e.getMessage(), e);
            }
        }).join();

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

    @PostMapping(value = "/history")
    public ResponseEntity<String> saveHistory(
            @Login Member loginMember,
            @RequestBody Integer bookId) {

        long memberId = loginMember.getId();

        if (memberId == 0 || bookId == 0) {
            log.debug("memberId = {}", memberId);
            return new ResponseEntity<>("fail", HttpStatus.OK);
            //throw new UserException("로그인 아이디 또는 책id가 없습니다.");
        } else {
            historyService.saveHistory(loginMember, bookId);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
