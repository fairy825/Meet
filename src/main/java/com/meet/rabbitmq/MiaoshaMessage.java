package com.meet.rabbitmq;

import com.meet.pojo.Book;
import com.meet.pojo.User;

public class MiaoshaMessage {
	private User user;
	private Integer bookId;
	private int date;
	private String remark;

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
