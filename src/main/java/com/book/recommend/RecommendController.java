package com.book.recommend;

import com.book.exception.UserException;
import com.book.history.service.HistoryService;
import com.book.model.Category;
import com.book.model.Member;
import com.book.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = {"/recommend", "/"})
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;
    private final HistoryService historyService;
    //private final CategoryMapper categoryMapper;

    @Value("${bookRecommend.host}")
    private String hostName;

    @GetMapping(value = "/")
    public String index(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            Model model, Category category, HttpSession session) {

        //@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember
        /*
            HttpSession session1 = request.getSession(false);
            if (session == null) {
                return "home";
            }
            Member loginMember = (Member)session1.getAttribute(SessionConst.LOGIN_MEMBER);
         */

        //로그인 유무에 따른 로직 구현
        long memberId = loginMember == null ? 0 : loginMember.getId();

        List<RecommendDto> recommendList = recommendService.getRecommendList(memberId, category);
        model.addAttribute("recommendList", recommendList);

//        List<Category> list = categoryMapper.getDistinctCategory();
//        model.addAttribute("categoryList", list);
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
