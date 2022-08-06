package com.book.recommend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("recommendDAO")
public interface RecommendDAO {
	public int saveBook(Map map) throws DataAccessException;
	public int saveRecommend(RecommendVO recommendVO) throws DataAccessException;
	public void saveHistory(long itemId) throws DataAccessException;
	public List<Map<String,Object>> getFilteredList(String user_id);
	public List<Map<String,Object>> getRecommendList(List filteredCateList);
}
