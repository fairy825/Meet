package com.meet.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.meet.mapper.CategoryMapper;
import com.meet.pojo.Category;
import com.meet.pojo.News;
import com.meet.service.CategoryService;
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

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public PagedResult list(String keyword, int start, int size) {
        PageHelper.startPage(start, size);
        List<Category> list = categoryMapper.queryCategoryByName(keyword);
        PageInfo<Category> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(start);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public List<Category> findByType(String type){
        Category category = new Category();
        category.setType(type);
        return categoryMapper.select(category);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public List<Category> findAll() {
        return categoryMapper.selectAll();
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void saveCategory(Category category) {
        categoryMapper.insertUseGeneratedKeys(category);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void delete(int id){
        categoryMapper.deleteByPrimaryKey(id);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Category get(int id){
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void update(Category category){
        categoryMapper.updateByPrimaryKeySelective(category);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public List<Category> findByBook(int book_id){
        return categoryMapper.queryByBook(book_id);
    }
}