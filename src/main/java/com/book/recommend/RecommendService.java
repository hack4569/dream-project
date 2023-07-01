package com.book.recommend;

import com.book.aladin.domain.AladinBook;
import com.book.book.BookFilterDto;
import com.book.common.ApiParam;
import com.book.model.Category;
import com.book.model.History;
import com.book.model.Recommend;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RecommendService {
	public Map<String,Object> selectDetail(int ItemId) throws Exception;
	public List<RecommendVO> recommendList2(String userId) throws Exception;
	public int saveBook(Map map) throws Exception;
	public int saveRecommend(RecommendVO recommendVO) throws Exception;
	public Recommend saveRecommend(Recommend recommend) throws Exception;
	public History saveHistory(History history);
	public List<RecommendDto> getRecommendList(String user_id, Category category) throws Exception;
	public Object getSearchBookList(HttpServletRequest request, ApiParam apiParam);
	public List<AladinBook> bestSellerList(BookFilterDto bookFilterDto);
}
