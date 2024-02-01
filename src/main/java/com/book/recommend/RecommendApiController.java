package com.book.recommend;

import com.book.book.CategoryRepository;
import com.book.exception.UserException;
import com.book.history.service.HistoryService;
import com.book.model.Category;
import com.book.model.History;
import com.book.model.Member;
import com.book.model.mapper.CategoryMapper;
import com.book.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = {"/api/recommend"})
@RequiredArgsConstructor
public class RecommendApiController {
    private final RecommendService recommendService;
    private final CategoryRepository categoryRepository;
    private final HistoryRepository historyRepository;
    private final HistoryService historyService;
    private final CategoryMapper categoryMapper;
    @Value("${bookRecommend.host}")
    private String hostName;

    @RequestMapping(value = "/list")
    public @ResponseBody List<RecommendDto> list(
            Category category,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) throws Exception {


        //로그인 유무에 따른 로직 구현
        long memberId = loginMember == null ? 0 : loginMember.getId();

        List<RecommendDto> list = recommendService.getRecommendList(memberId, category);

        return list;
    }

    @PostMapping(value = "/history/{bookId}")
    public void saveHistory(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            @PathVariable("bookId") long bookId) {

        long memberId = loginMember == null ? 0 : loginMember.getId();

        if (memberId == 0 || bookId == 0) {
            log.debug("memberId = {}", memberId);
            throw new UserException("로그인 아이디 또는 책id가 없습니다.");
        }

        historyService.saveHistory(loginMember, bookId);
    }
}
