package com.meet.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meet.service.CommentService;
import com.meet.utils.TimeAgoUtils;

import java.util.Date;
import javax.persistence.*;

public class Comment {
    @Id
    private Integer id;

    private String content;
    private Integer rating;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "create_date")
    @JsonFormat( pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createDate;

    private String state;

    @Column(name = "appoint_id")
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