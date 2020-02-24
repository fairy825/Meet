package com.meet.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

public class Book {
    @Id
    private Integer id;

    private String isbn;

    @Column(name = "douban_id")
    private Integer doubanId;

    private String title;

    private String description;

    private String author;

    @Column(name = "publishing_house")
    private String publishingHouse;

    @Column(name = "total_num")
    private Integer totalNum;

    private Integer stock;

    private Float rating;

    private String tag;

    @Column(name = "rec_book")
    private String recBook;

    @Column(name = "review_tag")
    private String reviewTag;

    @Column(name = "eigen_vec")
    private String eigenVec;

    @Column(name = "emotion_vec")
    private String emotionVec;

    private Byte recommended;

    @Column(name = "updated_time")
    @JsonFormat( pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date updatedTime;

    @Transient
    private Integer reviewCount;
    @Transient
    private Integer saleCount;
    @Transient
    private Bookimage firstBookImage;
    @Transient
    private List<Bookimage> bookImages;
    @Transient
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

    public Bookimage getFirstBookImage() {
        return firstBookImage;
    }

    public void setFirstBookImage(Bookimage firstBookImage) {
        this.firstBookImage = firstBookImage;
    }

    public List<Bookimage> getBookImages() {
        return bookImages;
    }

    public void setBookImages(List<Bookimage> bookImages) {
        this.bookImages = bookImages;
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
     * @return tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return rec_book
     */
    public String getRecBook() {
        return recBook;
    }

    /**
     * @param recBook
     */
    public void setRecBook(String recBook) {
        this.recBook = recBook;
    }

    /**
     * @return review_tag
     */
    public String getReviewTag() {
        return reviewTag;
    }

    /**
     * @param reviewTag
     */
    public void setReviewTag(String reviewTag) {
        this.reviewTag = reviewTag;
    }

    /**
     * @return eigen_vec
     */
    public String getEigenVec() {
        return eigenVec;
    }

    /**
     * @param eigenVec
     */
    public void setEigenVec(String eigenVec) {
        this.eigenVec = eigenVec;
    }

    /**
     * @return emotion_vec
     */
    public String getEmotionVec() {
        return emotionVec;
    }

    /**
     * @param emotionVec
     */
    public void setEmotionVec(String emotionVec) {
        this.emotionVec = emotionVec;
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