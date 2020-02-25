package com.meet.service;

import com.meet.pojo.Book;
import com.meet.pojo.bo.BookBO;
import com.meet.pojo.vo.BookVO;
import com.meet.utils.PagedResult;

import java.util.List;

public interface BookService {
    public PagedResult list(int start, int size) ;
    public PagedResult listByCategory(int cid, int start, int size);
    public PagedResult search(Integer cid,  BookVO bookVO,
                              int stock, int minRating,String sort, int start, int size) ;

    //    public Page4Navigator<Venue> search(int did, String sort, Venue venue, Integer minPrice, Integer maxPrice, int start, int size, int navigatePages);
//    public Page4Navigator<Venue> searchByKeyword(int did, String sort, String keyword, Integer minPrice, Integer maxPrice, int start, int size, int navigatePages);
    public void saveBook(Book book);
    public void delete(int id);
    public Book get(int id);
    public void update(BookBO bookBO);
    public boolean reduceStock(int id);
    public boolean addStock(int id);
//    public Book findByName(String name);
    public List<Book> findByTitleLike(String title);
    public List<Book> list();
    public void setCategory(Book book);
    public void setSaleAndReviewNumber(Book book);
    public void setSaleAndReviewNumber(List<Book> books);
    public void addRating(int id,int rating);
    public void deleteRating(int id,int rating);
    }