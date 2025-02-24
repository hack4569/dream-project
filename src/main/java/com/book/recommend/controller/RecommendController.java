package com.book.recommend.controller;

import com.book.category.dto.CategoryDto;
import com.book.category.service.CategoryService;
import com.book.history.repository.HistoryRepository;
import com.book.history.service.HistoryService;
import com.book.model.History;
import com.book.model.Member;
import com.book.recommend.dto.RecommendDto;
import com.book.recommend.dto.RecommendParam;
import com.book.recommend.service.RecommendService;
import com.book.user.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Controller
@RequestMapping(value = {"/recommend", "/"})
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;
    private final HistoryService historyService;
    private final HistoryRepository historyRepository;
    private final CategoryService categoryService;

    @GetMapping(value = "/")
    public String index(
            @Login Member loginMember,
            Model model, CategoryDto categoryDto) {

        List<RecommendDto> recommendList = new ArrayList<>();
        List<History> histories = historyRepository.findHistoryByMemberId(loginMember.getId());
        String subCid = Optional.ofNullable(categoryDto.getSubCid()).orElse("");

        HashSet<Integer> cids = new HashSet<>();
        //사용자가 희망하는 카테고리가 있을 경우
        if (StringUtils.hasText(subCid)) {
        } else {
            cids.addAll(categoryService.findCategories());
        }

        CompletableFuture<List<RecommendDto>> recommendList1 = CompletableFuture.supplyAsync(() -> recommendService.getRecommendList(RecommendParam.builder()
                .member(loginMember)
                .categoryDto(categoryDto)
                .histories(histories)
                .slideN(1)
                .cids(cids)
                .build()));
        /**
         * CompletableFuture<List<RecommendDto>> recommendList2 = CompletableFuture.supplyAsync(() -> recommendService.getRecommendList(RecommendParam.builder()
         *                 .member(loginMember)
         *                 .categoryDto(categoryDto)
         *                 .histories(histories)
         *                 .slideN(2)
         *                 .cids(cids)
         *                 .build()));
         *         CompletableFuture<List<RecommendDto>> recommendList3 = CompletableFuture.supplyAsync(() -> recommendService.getRecommendList(RecommendParam.builder()
         *                 .member(loginMember)
         *                 .categoryDto(categoryDto)
         *                 .histories(histories)
         *                 .slideN(3)
         *                 .cids(cids)
         *                 .build()));
         */


        CompletableFuture<Void> allRecommendList = CompletableFuture.allOf(recommendList1);
        allRecommendList.thenRun(() -> {
            try {
                recommendList.addAll(recommendList1.get());
//                recommendList.addAll(recommendList2.get());
//                recommendList.addAll(recommendList3.get());
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
