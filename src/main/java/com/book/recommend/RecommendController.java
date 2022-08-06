package com.book.recommend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.book.common.ApiCommonUtil;
import com.book.common.ApiParam;
import com.book.model.Recommend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Controller
@RequestMapping(value = "/recommend")
public class RecommendController {

	@Autowired
	RecommendService recommendService;

	@RequestMapping(value= "/index.do")
	public String index() {
		return "recommend/index";
	}
	
	@GetMapping(value="/register.do")
	public String register() {
		return "recommend/register";
	}
	
	@RequestMapping(value="/save.do")
	public String save(@ModelAttribute RecommendVO recommendVO, HttpServletRequest request) throws Exception{
		Recommend recommend = Recommend.builder()
				.itemId(recommendVO.getItemId())
				.commentBrief(recommendVO.getBrief())
				.commentDetail(recommendVO.getDetail())
				.userId("admin")
				.build();
		recommend = recommendService.saveRecommend(recommend);
		return "Y";
		/*
		String sItemId = request.getParameter("itemId");
		int itemId = Integer.parseInt(sItemId);
		
		String uri = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=ttbhack45691028002&itemIdType=itemId&ItemId="+itemId+"&output=js&Version=20131101&OptResult=reviewList";
		//책정보
		Map<String,Object> map1 = new HashMap<>();
		//책리뷰
		recommendVO.setUserId("admin");
		
		//책정보 조회
		map1 = recommendService.selectDetail(itemId);
		
		//책정보 저장
		int res1 = recommendService.saveBook(map1);
		
		//책리뷰 저장
		int res2 = recommendService.saveRecommend(recommendVO);

		return null;

		 */
	}
	
	@RequestMapping(value="/list")
	public @ResponseBody List<Map<String,Object>> list() throws Exception{

		//로그인 유무에 따른 로직 구현
		String user_id = "admin";
		List<Map<String,Object>> list =  recommendService.getRecommendList(user_id);
		                                                               
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value="/saveHistory.do")
	public void saveHistory(@RequestParam("bookId") long bookId, HttpServletRequest request) throws Exception{
		long itemId = bookId;
		recommendService.saveHistory(itemId);
	}

	@RequestMapping(value="/search.do")
	public @ResponseBody Object search(HttpServletRequest request,ApiParam apiParam, Model model) throws Exception{
		return recommendService.getSearchBookList(request, apiParam);
	}

	public static Map<String, Object> getMapFromJSONObject(JSONObject obj) {
		if (obj==null) { 
			throw new IllegalArgumentException(String.format("BAD REQUEST obj %s", obj)); 
		} try { 
			return new ObjectMapper().readValue(obj.toJSONString(), Map.class); 
		} catch (Exception e) {
			throw new RuntimeException(e); } 
	}

}
