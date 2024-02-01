package com.book.recommend;

import com.book.book.CategoryRepository;
import com.book.common.ApiParam;
import com.book.model.Category;
import com.book.model.Member;
import com.book.model.mapper.CategoryMapper;
import com.book.recommend.exception.RecommendExcption;
import com.book.session.SessionConst;
import com.book.user.login.argumentresolver.Login;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = {"/recommend", "/"})
public class RecommendController {

    @Autowired
    RecommendService recommendService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMapper categoryMapper;

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

        try {
            List<RecommendDto> recommendList = recommendService.getRecommendList(memberId, category);
            model.addAttribute("recommendList", recommendList);

            List<Category> list = categoryMapper.getDistinctCategory();
            model.addAttribute("categoryList", list);
            model.addAttribute("subCid", category.getSubCid());
            model.addAttribute("hostName", hostName);
            model.addAttribute("loginMember", loginMember);
            if (loginMember == null) {
                model.addAttribute("loginHistoryMsg", "로그인하시면 봤던 책정보는 보이지 않습니다.");
            }
        } catch (Exception e) {
            log.error("[/] ERROR : {}", e);
            throw new RuntimeException("서버내부 오류발생");
        }

        return "recommend/index";
    }

    @RequestMapping(value = "/list")
    public @ResponseBody List<RecommendDto> list(
            Category category,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) throws Exception {


        //로그인 유무에 따른 로직 구현
        long memberId = loginMember == null ? 0 : loginMember.getId();

        List<RecommendDto> list = recommendService.getRecommendList(memberId, category);

        return list;
    }
}
