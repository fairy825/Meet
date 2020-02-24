package com.meet.service;

import com.meet.pojo.News;
import com.meet.utils.PagedResult;

public interface NewsService {
    public PagedResult list(String keyword, int start, int size);
    public void saveNews(News news);
    public void delete(int id);
    public News get(int id);
}