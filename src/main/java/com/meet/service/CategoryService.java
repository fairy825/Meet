package com.meet.service;

import com.meet.pojo.Category;
import com.meet.pojo.User;
import com.meet.utils.PagedResult;

import java.util.List;

public interface CategoryService {
    public static final String literature = "literature";
    public static final String popular = "popular";
    public static final String business = "business";
    public static final String culture = "culture";
    public static final String life = "life";
    public static final String science = "science";

    public PagedResult list(String keyword, int start, int size);
    public void saveCategory(Category category);
    public void delete(int id);
    public Category get(int id);
    public void update(Category category);
    public List<Category> findAll();
    public List<Category> findByType(String type);
    public List<Category> findByBook(int book_id);
}