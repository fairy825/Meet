package com.meet.mapper;

import com.meet.pojo.News;
import com.meet.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewsMapper extends MyMapper<News> {
    public List<News> queryNewsByTitle(@Param("title")String title);
}