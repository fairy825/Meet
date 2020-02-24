package com.meet.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meet.pojo.Bookimage;
import com.meet.pojo.Category;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

public class BookVO {
    @Id
    private Integer id;

    private String isbn;

    private Integer doubanId;

    private String title;

    private String description;

    private String author;

    private String publishingHouse;

    private Integer totalNum;

    private Integer stock;

    private Float rating;

    private Byte recommended;

    @JsonFormat( pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date updatedTime;

    private Integer reviewCount;
    private Integer saleCount;
    private Bookimage firstVenueImage;
    private List<Bookimage> venueImages;
    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    public Bookimage getFirstVenueImage() {
        return firstVenueImage;
    }

    public void setFirstVenueImage(Bookimage firstVenueImage) {
        this.firstVenueImage = firstVenueImage;
    }

    public List<Bookimage> getVenueImages() {
        return venueImages;
    }

    public void setVenueImages(List<Bookimage> venueImages) {
        this.venueImages = venueImages;
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
     * @return douban_id
     */
    public Integer getDoubanId() {
        return doubanId;
    }

    /**
     * @param doubanId
     */
    public void setDoubanId(Integer doubanId) {
        this.doubanId = doubanId;
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
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return total_num
     */
    public Integer getTotalNum() {
        return totalNum;
    }

    /**
     * @param totalNum
     */
    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
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

    /**
     * @return recommended
     */
    public Byte getRecommended() {
        return recommended;
    }

    /**
     * @param recommended
     */
    public void setRecommended(Byte recommended) {
        this.recommended = recommended;
    }

    /**
     * @return updated_time
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}