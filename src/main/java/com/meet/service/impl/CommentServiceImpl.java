package com.meet.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.meet.mapper.CommentMapper;
import com.meet.mapper.CommentMapperCustom;
import com.meet.pojo.Appoint;
import com.meet.pojo.Book;
import com.meet.pojo.Comment;
import com.meet.pojo.vo.CommentVO;
import com.meet.service.AppointService;
import com.meet.service.CommentService;
import com.meet.utils.PagedResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;
    @Autowired
    CommentMapperCustom commentMapperCustom;
    @Autowired
    AppointService appointService;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult listByBook(int bid, int status, int start, int size) {
        PageHelper.startPage(start, size);
//        Example commentExample = new Example(Comment.class);
//        Criteria criteria = commentExample.createCriteria();
//        criteria.andEqualTo("bookId", bid);
//        List<Comment> list = new ArrayList<>();
//        if (status == 1)
//            criteria.andEqualTo("state", pass);
//        list = commentMapper.selectByExample(commentExample);
        List<CommentVO> list = commentMapperCustom.listByBook(bid,0);
        PageInfo<CommentVO> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(start);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult list(int start, int size) {
        PageHelper.startPage(start, size);
        List<Comment> list = commentMapper.selectAll();
        PageInfo<Comment> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(start);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComment(Comment comment) {
        commentMapper.insertUseGeneratedKeys(comment);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(int id) {
        commentMapper.deleteByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Comment get(int id) {
        return commentMapper.selectByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(Comment comment) {
        commentMapper.updateByPrimaryKey(comment);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer total(Book book) {
        Example commentExample = new Example(Comment.class);
        Criteria criteria = commentExample.createCriteria();
        criteria.andEqualTo("bookId", book.getId());
        criteria.andEqualTo("state", pass);

        return commentMapper.selectCountByExample(commentExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Comment queryByAppoint(Integer appointId) {
        Comment comment = new Comment();
        comment.setAppointId(appointId);
        Comment result = commentMapper.selectOne(comment);

        return result;
    }

}
