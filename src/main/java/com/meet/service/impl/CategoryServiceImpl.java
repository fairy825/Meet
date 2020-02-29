package com.meet.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.meet.mapper.CategoryMapper;
import com.meet.pojo.Category;
import com.meet.pojo.News;
import com.meet.service.CategoryService;
import com.meet.utils.PagedResult;
import com.meet.utils.RedisConstant;
import com.meet.utils.RedisOperator;
import com.meet.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    RedisOperator redis;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult list(String keyword, int start, int size) {
        if (keyword == null || keyword == "") {
            //取缓存
            PagedResult result = JSONObject.parseObject(redis.get(RedisConstant.CATEGORY_PAGE + ":" + start + "-" + size),
                    PagedResult.class);
//            PagedResult result = redis.get(RedisConstant.CATEGORY_PAGE + ":" + start + "-" + size, PagedResult.class);
            if (result != null) {
                return result;
            }
            //取数据库
            PageHelper.startPage(start, size);
            List<Category> list = categoryMapper.queryCategoryByName(keyword);
            PageInfo<Category> pageList = new PageInfo<>(list);

            result = new PagedResult();
            result.setPage(start);
            result.setTotal(pageList.getPages());
            result.setRows(list);
            result.setRecords(pageList.getTotal());
            if (result != null) {
                redis.setBean(RedisConstant.CATEGORY_PAGE + ":" + start + "-" + size, result);
            }
            return result;
        } else {
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
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> findByType(String type) {
        Category category = new Category();
        category.setType(type);
        return categoryMapper.select(category);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> findAll() {
        //取缓存
        String str = redis.get(RedisConstant.CATEGORY_ALL_INFO);
        List<Category> result = JSONObject.parseArray(str, Category.class);
        if (result != null) {
            return result;
        }
        //取数据库
        result = categoryMapper.selectAll();
        if (result != null) {
            redis.setBean(RedisConstant.CATEGORY_ALL_INFO, result);
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveCategory(Category category) {
        categoryMapper.insertUseGeneratedKeys(category);
        Set<String> str = redis.keys("category-page*");
        for (String s : str)
            redis.del(s);
        redis.del(RedisConstant.CATEGORY_ALL_INFO);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(int id) {
        categoryMapper.deleteByPrimaryKey(id);
        //处理缓存
        Set<String> str = redis.keys("category-page*");
        for (String s : str)
            redis.del(s);
        redis.del(RedisConstant.CATEGORY_INFO_ID + ":" + id);
        redis.del(RedisConstant.CATEGORY_ALL_INFO);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Category get(int id) {
        //取缓存
        Category result = redis.get(RedisConstant.CATEGORY_INFO_ID + ":" + id, Category.class);
        if (result != null) {
            return result;
        }
        //取数据库
        result = categoryMapper.selectByPrimaryKey(id);
        if (result != null) {
            redis.setBean(RedisConstant.CATEGORY_INFO_ID + ":" + id, result);
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(Category category) {
        //取category
        Category curCategory = get(category.getId());
        //更新数据库
        categoryMapper.updateByPrimaryKeySelective(category);
        //处理缓存
        Set<String> str = redis.keys("category-page*");
        for (String s : str)
            redis.del(s);
//        redis.del(RedisConstant.CATEGORY_INFO_ID + ":" + curCategory.getId());
        redis.del(RedisConstant.CATEGORY_ALL_INFO);
//        BeanUtils.copyProperties(user,curUser);
        SpringUtil.copyPropertiesIgnoreNull(category, curCategory);
        redis.setBean(RedisConstant.CATEGORY_INFO_ID + ":" + curCategory.getId(), curCategory);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> findByBook(int book_id) {
        return categoryMapper.queryByBook(book_id);
    }
}