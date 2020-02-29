package com.meet.service.impl;

import com.meet.mapper.BookimageMapper;
import com.meet.pojo.Book;
import com.meet.pojo.Bookimage;
import com.meet.service.BookService;
import com.meet.service.BookimageService;
import com.meet.utils.RedisConstant;
import com.meet.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookimageServiceImpl implements BookimageService {

    @Autowired
    BookimageMapper bookimageMapper;
    @Autowired
    BookService bookService;
    @Autowired
    RedisOperator redis;

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public  List<Bookimage> listByBook(int bid){
        Bookimage bookimage = new Bookimage();
        bookimage.setBookId(bid);
        return bookimageMapper.select(bookimage);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void add(Bookimage bookimage){
        bookimageMapper.insertUseGeneratedKeys(bookimage);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void delete(int id){
        bookimageMapper.deleteByPrimaryKey(id);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Bookimage get(int id){
        return bookimageMapper.selectByPrimaryKey(id);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void setFirstBookimages(List<Book> books){
        for (Book book : books)
            setFirstBookimage(book);
    }
    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void setFirstBookimage(Book book){
        Bookimage result = redis.get(RedisConstant.BOOKIMAGE_COVER+":"+book.getId(),Bookimage.class);
        if(result != null) {
            return ;
        }

        //更新数据库
        List<Bookimage> singleImages = listByBook(book.getId());
        book.setBookImages(singleImages);
        if(!singleImages.isEmpty())
            book.setFirstBookImage(singleImages.get(0));
        else
            book.setFirstBookImage(new Bookimage());

    }
}