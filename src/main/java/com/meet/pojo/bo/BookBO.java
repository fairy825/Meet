package com.meet.pojo.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meet.pojo.Bookimage;
import com.meet.pojo.Category;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

public class BookBO {
    @Id
    private Integer id;

    private String isbn;

    private String title;

    private String author;

    private String publishingHouse;

    private Integer stock;

    private Float rating;

    private List<Integer> categories;

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    /**
     * @return id
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
     * @return isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return publishing_house
     */
    public String getPublishingHouse() {
        return publishingHouse;
    }

    /**
     * @param publishingHouse
     */
    public void setPublishingHouse(String publishingHouse) {
        this.publishingHouse = publishingHouse;
    }

    /**
     * @return stock
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * @param stock
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * @return rating
     */
    public Float getRating() {
        return rating;
    }

    /**
     * @param rating
     */
    public void setRating(Float rating) {
        this.rating = rating;
    }
}