package com.meet.pojo;

import javax.persistence.*;

public class Bookimage {
    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "book_id")
    private Integer bookId;

    /**
     * @return Id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return book_id
     */
    public Integer getBookId() {
        return bookId;
    }

    /**
     * @param bookId
     */
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }
}