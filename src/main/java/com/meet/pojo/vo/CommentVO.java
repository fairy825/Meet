package com.meet.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meet.service.CommentService;
import com.meet.utils.TimeAgoUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class CommentVO {
    private Integer id;

    private String content;
    private Integer rating;
    private Integer userId;
    private String userName;
    private Integer bookId;
    private String bookName;
    private String bookIsbn;
    @JsonFormat( pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createDate;
    private String state;
    private Integer appointId;

    public String getStateDesc(){
        String desc ="未知";
        switch(state){
            case CommentService.pass:
                desc="通过";
                break;
            case CommentService.refused:
                desc="封禁";
                break;
            default:
                desc="未知";
        }
        return desc;
    }
    public String getTimeDesc() {
        return TimeAgoUtils.format(createDate);
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
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
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
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

    /**
     * @return create_date
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return appoint_id
     */
    public Integer getAppointId() {
        return appointId;
    }

    /**
     * @param appointId
     */
    public void setAppointId(Integer appointId) {
        this.appointId = appointId;
    }
}