package com.meet.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.meet.mapper.CommentMapper;
import com.meet.mapper.CommentMapperCustom;
import com.meet.pojo.Appoint;
import com.meet.pojo.Book;
import com.meet.pojo.Comment;
import com.meet.pojo.User;
import com.meet.pojo.vo.CommentVO;
import com.meet.service.AppointService;
import com.meet.service.BookService;
import com.meet.service.CommentService;
import com.meet.service.UserService;
import com.meet.utils.PagedResult;
import com.meet.utils.RedisConstant;
import com.meet.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;
    @Autowired
    RedisOperator redis;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult listByBook(int bid, int status, int start, int size) {
        PageHelper.startPage(start, size);
        List<CommentVO> list = commentMapperCustom.listByBook(bid, status);
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
    public PagedResult listNewCommentByBook(int bid,  int size) {
            List<String> strs = redis.lRange(RedisConstant.BOOK_COMMENT + ":" + bid, 0, size - 1);

            List<CommentVO> list = new ArrayList<>();
            for (String s : strs) {
                list.add(redis.stringToBean(s, CommentVO.class));
            }
            PagedResult pagedResult = new PagedResult();
            pagedResult.setPage(1);
            pagedResult.setTotal(1);
            pagedResult.setRows(list);
            pagedResult.setRecords(size);

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
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);
        Book book = bookService.get(comment.getBookId());
        User user = userService.queryUserInfo(comment.getUserId());
        commentVO.setBookIsbn(book.getIsbn());
        commentVO.setBookName(book.getTitle());
        commentVO.setUserName(user.getName());
        redis.lpush(RedisConstant.BOOK_COMMENT + ":" + comment.getBookId(), redis.beanToString(commentVO));
        commentMapper.insertUseGeneratedKeys(comment);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(int id) {
//        redis.del(RedisConstant.BOOK_COMMENT + ":" + get(id).getBookId());
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
