package com.book.model.mapper;

import com.book.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CategoryMapper {
    List<Category> getDistinctCategory();
    List<Category> getCategoryByParam(Category category);
}
