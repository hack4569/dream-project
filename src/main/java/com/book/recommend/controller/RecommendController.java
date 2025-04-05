package com.book.recommend.controller;

import com.book.category.dto.CategoryDto;
import com.book.category.service.CategoryService;
import com.book.history.repository.HistoryRepository;
import com.book.history.service.HistoryService;
import com.book.model.History;
import com.book.model.Member;
import com.book.recommend.constants.RcmdConst;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        List<CompletableFuture<List<RecommendDto>>> futures = IntStream.rangeClosed(RcmdConst.THREAD_START_IDX, RcmdConst.THREAD_END_IDX)
                .mapToObj(n -> CompletableFuture.supplyAsync(() ->
                        recommendService.getRecommendList(RecommendParam.builder()
                                .member(loginMember)
                                .categoryDto(categoryDto)
                                .histories(histories)
                                .slideN(n)
                                .cids(cids)
                                .build()))
//                                .orTimeout(10, TimeUnit.SECONDS) // ⏱ 5초 초과하면 예외 발생
//                                .exceptionally(ex -> {
//                                    log.warn("RecommendList {}번 시간 초과 또는 오류: {}", n, ex.toString());
//                                    return null; // 실패한 애는 null 반환
//                                })
                            )
                .collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        allFutures.thenRun(() -> {
            try {
                for (CompletableFuture<List<RecommendDto>> future : futures) {
                    recommendList.addAll(future.get());
                }
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
