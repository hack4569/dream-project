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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.book.book.CategoryRepository;
import com.book.common.ApiCommonUtil;
import com.book.common.ApiParam;
import com.book.model.Category;
import com.book.model.Recommend;
import com.book.model.mapper.CategoryMapper;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

	@GetMapping(value= "/")
	public String index(Model model, Category category) {
		List<Category> list = categoryMapper.getDistinctCategory();
		model.addAttribute("categoryList",list);
		model.addAttribute("subCid",category.getSubCid());
		return "recommend/index";
	}
	
	@RequestMapping(value="/list")
	public @ResponseBody List<RecommendDto> list(Category category) throws Exception{

		//로그인 유무에 따른 로직 구현
		String user_id = "admin";

		category.setSubCid("1");
		List<RecommendDto> list =  recommendService.getRecommendList(user_id, category);

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


}
