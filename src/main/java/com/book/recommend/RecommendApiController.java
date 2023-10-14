package com.book.recommend;

import com.book.book.CategoryRepository;
import com.book.model.Category;
import com.book.model.History;
import com.book.model.Member;
import com.book.model.mapper.CategoryMapper;
import com.book.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    private final CategoryMapper categoryMapper;
    @Value("${bookRecommend.host}")
    private String hostName;

    @RequestMapping(value="/list")
    public @ResponseBody List<RecommendDto> list(
            Category category,
            @SessionAttribute(name= SessionConst.LOGIN_MEMBER, required=false) Member loginMember) throws Exception{


        //로그인 유무에 따른 로직 구현
        String loginId = loginMember ==null ? "" : loginMember.getLoginId();

        List<RecommendDto> list =  recommendService.getRecommendList(loginId, category);

        return list;
    }

    @ResponseBody
    @PostMapping(value="/history")
    public void saveHistory(
            @SessionAttribute(name=SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            @RequestParam("bookId") long bookId,
            HttpServletRequest request, HttpSession session){

        String loginId = loginMember ==null ? "" : loginMember.getLoginId();

        if(!StringUtils.hasText(loginId) || bookId ==0){
            return;
        }
        History history = new History();
        history.setItemId(bookId);
        history.setLoginId(loginId);
        recommendService.saveHistory(history);
    }
}
