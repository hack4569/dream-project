package com.book.recommend;

import com.book.book.CategoryRepository;
import com.book.common.ApiParam;
import com.book.model.Category;
import com.book.model.History;
import com.book.model.Member;
import com.book.model.mapper.CategoryMapper;
import com.book.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

	@GetMapping(value= "/")
	public String index(
			@SessionAttribute(name=SessionConst.LOGIN_MEMBER, required=false) Member loginMember,
			Model model, Category category, HttpSession session) throws Exception{
		List<Category> list = categoryMapper.getDistinctCategory();
		model.addAttribute("categoryList",list);
		model.addAttribute("subCid",category.getSubCid());
		model.addAttribute("hostName",hostName);

		if(loginMember==null){
			model.addAttribute("loginHistoryMsg","로그인하시면 봤던 책정보는 보이지 않습니다.");
		}

		return "recommend/index";
	}
	
	@RequestMapping(value="/list")
	public @ResponseBody List<RecommendDto> list(
			Category category,
			@SessionAttribute(name=SessionConst.LOGIN_MEMBER, required=false) Member loginMember) throws Exception{


        //로그인 유무에 따른 로직 구현
        String loginId = loginMember ==null ? "" : loginMember.getLoginId();

        List<RecommendDto> list =  recommendService.getRecommendList(loginId, category);

        return list;
	}
	
	@ResponseBody
	@RequestMapping(value="/saveHistory")
	public void saveHistory(@SessionAttribute(name=SessionConst.LOGIN_MEMBER, required = false) Member member, @RequestParam("bookId") long bookId, HttpServletRequest request, HttpSession session) throws Exception{

		String loginId = "";

		if(member != null){
			loginId = member.getLoginId();
		}

		if(!StringUtils.hasText(loginId) || bookId ==0){
			return;
		}
		History history = new History();
		history.setItemId(bookId);
		history.setLoginId(loginId);
		recommendService.saveHistory(history);
	}

	@RequestMapping(value="/search.do")
	public @ResponseBody Object search(HttpServletRequest request,ApiParam apiParam, Model model) throws Exception{
		return recommendService.getSearchBookList(request, apiParam);
	}


}
