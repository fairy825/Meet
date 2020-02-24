package com.meet.service;


import com.meet.pojo.Appoint;
import com.meet.pojo.Book;
import com.meet.pojo.vo.AppointVO;
import com.meet.utils.PagedResult;

import java.util.List;

public interface AppointService {
    public static final String cancelled = "cancelled";//取消订单
    public static final String refused = "refused";//驳回订单
    public static final String waitTime = "waitTime";//未到取书时间
    public static final String waitArrive = "waitArrive";//待取书
    public static final String waitFinish = "waitFinish";//借书中
    public static final String late = "late";//逾期未归还
    public static final String waitReview = "waitReview";//待评价
    public static final String finish = "finish";//评价完毕
    public static final String delete = "delete";//删除订单

    public PagedResult list(int start, int size);
    public PagedResult search(AppointVO appointVO, int start, int size);
    public PagedResult searchWithoutDelete(AppointVO appointVO, int start, int size);
    public List<Appoint> findAllWithoutStatic();
    public Appoint get(int id);
    public void update(Appoint appoint);
//    public Page4Navigator<Booking> searchByUser(String keyword, int start, int size, int navigatePages);
//    public Page4Navigator<Booking> searchByVenue(String keyword, int start, int size, int navigatePages);
    public Integer total(Book book);
    public void saveAppoint(Appoint appoint);
//    public Page4Navigator<Booking> listBookingsByUser(User user, int start, int size, int navigatePages);
//    public Appoint searchByUserAndTimeslot(Appoint appoint);
    public List<Appoint> findAll();
    public boolean queryOrderIsExist(int userId, int bookId);

}