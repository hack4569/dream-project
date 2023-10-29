package com.book.recommend;

import com.book.book.CategoryRepository;
import com.book.exception.UserException;
import com.book.model.Category;
import com.book.model.History;
import com.book.model.Member;
import com.book.model.mapper.CategoryMapper;
import com.book.recommend.exception.RecommendExcption;
import com.book.session.SessionConst;
import com.book.user.login.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = {"/api/recommend"})
@RequiredArgsConstructor
public class RecommendApiController {
    private final RecommendService recommendService;
    private final CategoryRepository categoryRepository;
    private final HistoryRepository historyRepository;
    private final CategoryMapper categoryMapper;
    @Value("${bookRecommend.host}")
    private String hostName;

    @RequestMapping(value = "/list")
    public @ResponseBody List<RecommendDto> list(
            Category category,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) throws Exception {


        //로그인 유무에 따른 로직 구현
        String loginId = loginMember == null ? "" : loginMember.getLoginId();

        List<RecommendDto> list = recommendService.getRecommendList(loginId, category);

        return list;
    }

    @PostMapping(value = "/history/{bookId}")
    public void saveHistory(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            @PathVariable("bookId") long bookId) {

        String loginId = loginMember == null ? "" : loginMember.getLoginId();

        if (!StringUtils.hasText(loginId) || bookId == 0) {
            log.debug(loginId);
            throw new UserException("로그인 아이디 또는 책id가 없습니다.");
        }
        List<History> historyList = historyRepository.findHistoriesByLoginIdAndItemId(loginId, bookId);
        if (historyList.isEmpty()) {
            History history = new History();
            history.setItemId(bookId);
            history.setLoginId(loginId);
            historyRepository.save(history);
            log.debug("save history sucess itemId: "+bookId+"loginId: "+loginId);
        }
        log.debug("save history fail itemId: "+bookId+"loginId: "+loginId);
    }
}
