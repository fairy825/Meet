package com.meet.mapper;

import com.meet.pojo.Category;
import com.meet.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper extends MyMapper<Category> {
    public List<Category> queryCategoryByName(@Param("name")String name);
    public List<Category> queryByBook(@Param("bid")Integer bid);
}