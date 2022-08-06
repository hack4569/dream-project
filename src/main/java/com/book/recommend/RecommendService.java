package com.book.recommend;

import com.book.common.ApiParam;
import com.book.model.Recommend;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface RecommendService {
	public Map<String,Object> selectDetail(int ItemId) throws Exception;
	public List<RecommendVO> recommendList2(String userId) throws Exception;
	public int saveBook(Map map) throws Exception;
	public int saveRecommend(RecommendVO recommendVO) throws Exception;
	public Recommend saveRecommend(Recommend recommend) throws Exception;
	public void saveHistory(long itemId) throws Exception;
	public List<Map<String,Object>> getRecommendList(String user_id) throws Exception;
	public Object getSearchBookList(HttpServletRequest request, ApiParam apiParam);
}
