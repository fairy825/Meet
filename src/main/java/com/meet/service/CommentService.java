package com.meet.service;


import com.meet.pojo.Appoint;
import com.meet.pojo.Book;
import com.meet.pojo.Comment;
import com.meet.utils.PagedResult;

public interface CommentService {
    public static final String refused = "refused";
    public static final String pass = "pass";
    public PagedResult listByBook(int bid, int status, int start, int size);
    public PagedResult list(int start, int size);
    public void saveComment(Comment comment);
    public void delete(int id);
    public void update(Comment comment);
    public Comment get(int id);
    public Integer total(Book book);
    public Comment queryByAppoint(Integer appointId);

}