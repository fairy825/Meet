package com.meet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.meet.mapper.NewsMapper;
import com.meet.pojo.News;
import com.meet.service.NewsService;
import com.meet.utils.PagedResult;
import com.meet.utils.RedisConstant;
import com.meet.utils.RedisOperator;
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
import java.util.Set;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    NewsMapper newsMapper;
    @Autowired
    RedisOperator redis;

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public PagedResult list(String keyword, int start, int size) {
        if (keyword == null || keyword == "") {
            //取缓存
            PagedResult result = JSONObject.parseObject(redis.get(RedisConstant.NEWS_PAGE + ":" + start + "-" + size),
                    PagedResult.class);
            if (result != null) {
                return result;
            }
            //取数据库
            PageHelper.startPage(start, size);
            List<News> list = newsMapper.queryNewsByTitle(keyword);

            PageInfo<News> pageList = new PageInfo<>(list);

            result = new PagedResult();
            result.setPage(start);
            result.setTotal(pageList.getPages());
            result.setRows(list);
            result.setRecords(pageList.getTotal());
            if (result != null) {
                redis.setBean(RedisConstant.NEWS_PAGE + ":" + start + "-" + size, result);
            }
            return result;
        } else {

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
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void saveNews(News news) {
        news.setCreateDate(new Date());
        newsMapper.insertUseGeneratedKeys(news);
        Set<String> str = redis.keys("news-page*");
        for (String s : str)
            redis.del(s);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void delete(int id){
        //更新数据库
        newsMapper.deleteByPrimaryKey(id);
        //处理缓存
        Set<String> str = redis.keys("news-page*");
        for (String s : str)
            redis.del(s);
        redis.del(RedisConstant.NEWS_INFO_ID+":"+id);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public News get(int id){
        //取缓存
        News result = redis.get(RedisConstant.NEWS_INFO_ID+":"+id, News.class);
        if(result != null) {
            return result;
        }
        //取数据库
        result = newsMapper.selectByPrimaryKey(id);
        if(result != null) {
            redis.setBean(RedisConstant.NEWS_INFO_ID+":"+id, result);
        }
        return result;
    }

}