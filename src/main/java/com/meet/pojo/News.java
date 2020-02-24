package com.meet.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meet.utils.TimeAgoUtils;

import java.util.Date;
import javax.persistence.*;

public class News {
    @Id
    private Integer id;

    private String title;

    private String content;

    @Column(name = "create_date")
    @JsonFormat( pattern="yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createDate;

    public String getTimeDesc() {
        return TimeAgoUtils.format(createDate);
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
}