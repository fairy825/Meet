package com.meet.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meet.service.AppointService;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class AppointVO {
    private Integer id;
    private Integer userId;
    private Integer bookId;
    @JsonFormat( pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createDate;
    @JsonFormat( pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date arriveDate;
    @JsonFormat( pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;
    @JsonFormat( pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date backDate;
    @JsonFormat( pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;
    private String state;
    private String remark;
    private String userName;
    private String userNickname;
    private String bookIsbn;
    private String bookName;

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public String getStateDesc(){
        String desc ="未知";
        switch(state){
            case AppointService.cancelled://
                desc="已取消";
                break;
            case AppointService.waitTime://
                desc="未到取书时间";
                break;
            case AppointService.waitArrive:
                desc="待取书";
                break;
            case AppointService.waitFinish://
                desc="进行中";
                break;
            case AppointService.waitReview://
                desc="可留言";
                break;
            case AppointService.finish:
                desc="已结束";
                break;
            case AppointService.delete://
                desc="刪除";
                break;
            case AppointService.refused://
                desc="已驳回";
                break;
            default:
                desc="未知";
        }
        return desc;
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
     * @return start_date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getBackDate() {
		return backDate;
	}

	public void setBackDate(Date backDate) {
		this.backDate = backDate;
	}

    /**
     * @return end_date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(Date arriveDate) {
        this.arriveDate = arriveDate;
    }
}