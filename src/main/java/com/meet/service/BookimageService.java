package com.meet.service;


import com.meet.pojo.Book;
import com.meet.pojo.Bookimage;

import java.util.List;

public interface BookimageService {
    public  List<Bookimage> listByBook(int bid);
    public void add(Bookimage bookimage);
    public void delete(int id);
    public Bookimage get(int id);
    public void setFirstBookimages(List<Book> books);
    public void setFirstBookimage(Book book);
}