package com.meet.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.meet.comparator.BookAllComparator;
import com.meet.comparator.BookDateComparator;
import com.meet.comparator.BookReviewComparator;
import com.meet.comparator.BookSaleComparator;
import com.meet.mapper.BookMapper;
import com.meet.mapper.BookMapperCustom;
import com.meet.pojo.Book;
import com.meet.pojo.Category;
import com.meet.pojo.vo.BookVO;
import com.meet.service.*;
import com.meet.utils.PagedResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookMapper bookMapper;
    @Autowired
    BookMapperCustom bookMapperCustom;
    @Autowired
    CategoryService categoryService;
    @Autowired
    AppointService appointService;
    @Autowired
    CommentService commentService;
    @Autowired
    BookimageService bookimageService;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult listByCategory(int cid, int start, int size) {
        PageHelper.startPage(start, size);
        List<Book> list = bookMapper.queryByCategory(cid);
        setCategory(list);
        bookimageService.setFirstBookimages(list);
        PageInfo<Book> pageList = new PageInfo<>(list);

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
        List<Book> list = bookMapper.selectAll();
        setCategory(list);
        bookimageService.setFirstBookimages(list);
        PageInfo<Book> pageList = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(start);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());
        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Book> list() {
        return bookMapper.selectAll();
    }

    public void setSaleAndReviewNumber(Book book) {
        int saleCount = appointService.total(book);
        book.setSaleCount(saleCount);
        int reviewCount = commentService.total(book);
        book.setReviewCount(reviewCount);
    }

    public void setSaleAndReviewNumber(List<Book> books) {
        for (Book book : books) {
            setSaleAndReviewNumber(book);
        }
    }

    public void addRating(int bid, int rating) {
        Book book = get(bid);
        float average = book.getRating();
        int reviewCount = commentService.total(book);
        average = (average * reviewCount + rating) / (reviewCount + 1);
        book.setRating(average);
        bookMapper.updateByPrimaryKey(book);
    }

    public void deleteRating(int bid, int rating) {
        Book book = get(bid);
        float average = book.getRating();
        int reviewCount = commentService.total(book);
        if (reviewCount == 1)
            average = 0;
        else
            average = (average * reviewCount - rating) / (reviewCount - 1);
        book.setRating(average);
        bookMapper.updateByPrimaryKey(book);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult search(Integer cid, BookVO bookVO,
                              int stock, int minRating, String sort, int start, int size) {
        PageHelper.startPage(start, size);
        String isbn = bookVO.getIsbn();
        String title = bookVO.getTitle();
        String author = bookVO.getAuthor();
        String publishing_house = bookVO.getPublishingHouse();
        List<Book> list = null;
        if (cid == null)
            list = bookMapper.search( isbn, title, author, publishing_house, stock, minRating);
        else
            list = bookMapper.searchByCid(cid, isbn, title, author, publishing_house, stock, minRating);
        setSaleAndReviewNumber(list);
        setCategory(list);
        if(sort!=null)
        switch (sort) {
            case "review":
                Collections.sort(list, new BookReviewComparator());
                break;
            case "saleCount":
                Collections.sort(list, new BookSaleComparator());
                break;
            case "date":
                Collections.sort(list, new BookDateComparator());
                break;
            default:
                Collections.sort(list, new BookAllComparator());
                break;
        }
        PageInfo<Book> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(start);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Book> findByTitleLike(String title) {
        return bookMapper.queryByTitleLike(title);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveBook(Book book) {
        if (book.getRating() == null)
            book.setRating(Float.valueOf(0));
        if (book.getTotalNum() != null)
            book.setStock(book.getTotalNum());
        bookMapper.insertUseGeneratedKeys(book);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean reduceStock(int id) {
        Book book = bookMapper.selectByPrimaryKey(id);
        if (book.getStock() <= 0) return false;
        bookMapper.reduceStock(id);
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean addStock(int id) {
        Book book = bookMapper.selectByPrimaryKey(id);
        bookMapper.addStock(id);
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(int id) {
        bookMapper.deleteByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Book get(int id) {
        Book book = bookMapper.selectByPrimaryKey(id);
        return book;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(Book book) {
        bookMapper.updateByPrimaryKeySelective(book);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void setCategory(Book book) {
        List<Category> categories = categoryService.findByBook(book.getId());
        book.setCategories(categories);
    }

    public void setCategory(List<Book> books) {
        for (Book book : books) {
            setCategory(book);
        }
    }

//    public Book findByName(String name){
//        Book book = new Book();
//        book.setTitle(name);
//        return bookMapper.selec(name);
//    }
}
