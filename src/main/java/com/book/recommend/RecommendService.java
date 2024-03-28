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
	public List<RecommendDto> getRecommendList(long memberId, Category category);
	public List<AladinBook> bestSellerList(BookFilterDto bookFilterDto);
}
