package com.meet.comparator;

import java.io.Serializable;
import java.util.Comparator;
import com.meet.pojo.Book;

public class BookReviewComparator implements Serializable,Comparator<Book>{

    @Override
    public int compare(Book p1, Book p2) {
        return p2.getReviewCount()-p1.getReviewCount();
    }

}
