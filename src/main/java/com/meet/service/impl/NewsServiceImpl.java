package com.meet.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.meet.mapper.NewsMapper;
import com.meet.pojo.News;
import com.meet.service.NewsService;
import com.meet.utils.PagedResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    NewsMapper newsMapper;

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public PagedResult list(String keyword, int start, int size) {
        PageHelper.startPage(start, size);
        List<News> list = newsMapper.queryNewsByTitle(keyword);

        PageInfo<News> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(start);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void saveNews(News news) {
        news.setCreateDate(new Date());
        newsMapper.insertUseGeneratedKeys(news);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void delete(int id){
        newsMapper.deleteByPrimaryKey(id);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public News get(int id){
        return newsMapper.selectByPrimaryKey(id);
    }

}