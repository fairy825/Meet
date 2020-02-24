package com.meet.comparator;

import com.meet.pojo.Book;

import java.io.Serializable;
import java.util.Comparator;


public class BookAllComparator implements Serializable,Comparator<Book>{

    @Override
    public int compare(Book p1, Book p2) {
        return p2.getReviewCount()*p2.getSaleCount()-p1.getReviewCount()*p1.getSaleCount();
    }

}
