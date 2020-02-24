package com.meet.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.meet.mapper.AppointMapper;
import com.meet.mapper.AppointMapperCustom;
import com.meet.pojo.Appoint;
import com.meet.pojo.Book;
import com.meet.pojo.User;
import com.meet.pojo.vo.AppointVO;
import com.meet.service.AppointService;
import com.meet.service.BookService;
import com.meet.service.UserService;
import com.meet.utils.PagedResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AppointServiceImpl implements AppointService {

    @Autowired
    AppointMapper appointMapper;
    @Autowired
    AppointMapperCustom appointMapperCustom;
    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult list(int start, int size) {
        PageHelper.startPage(start, size);
//        List<Appoint> list = appointMapper.selectAll();
        List<AppointVO> list = appointMapperCustom.search(null,null,null, null);
        PageInfo<AppointVO> pageList = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(start);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Appoint> findAllWithoutStatic(){
        Example appointExample = new Example(Appoint.class);
        Criteria criteria = appointExample.createCriteria();
        String[] states = new String[]{waitTime, waitArrive, waitFinish,waitReview};
        criteria.andIn("state", Arrays.asList(states));
        return appointMapper.selectByExample(appointExample);
    }
    //    @Transactional(propagation = Propagation.SUPPORTS)
//    @Override
//    public Page4Navigator<Booking> searchWithoutDelete(Booking booking, int start, int size, int navigatePages) {
//        Sort sort = new Sort(Sort.Direction.DESC, "id");
//        Pageable pageable = new PageRequest(start, size, sort);
//        boolean flag = false;
//        Page pageFromJPA = null;
//        Booking b = new Booking();
//        b.setId(null);
//        if (StringUtils.isBlank(booking.getState())) booking.setState(null);
//        b.setState(booking.getState());
//
//        if (StringUtils.isBlank(booking.getUser().getName())) {
//            b.setUser(null);
//        } else {
//            User user = userService.findByName(booking.getUser().getName());
//            if (user != null) {//存在这个user
//                b.setUser(user);
//            } else {//不存在该user
//                flag = true;
//            }
//        }
//        if (StringUtils.isBlank(booking.getVenue().getName())) {
//            b.setVenue(null);
//        } else {
//            Venue venue = venueService.findByName(booking.getVenue().getName());
//            if (venue != null) {//存在这个venue
//                b.setVenue(venue);
//            } else {//不存在这个venue
//                flag = true;
//            }
//        }
//        b.setUser(userService.findByName(booking.getUser().getName()));
//        if(booking.getVenue()!=null)
//            b.setVenue(venueService.findByName(booking.getVenue().getName()));
//        else b.setVenue(null);
//        int count = 0;
//        List<Booking> res = new ArrayList<>();
//
//        if (!flag) {
//            Example<Booking> example = Example.of(b);
//            List<Booking> list = new ArrayList<>();
//            List<Booking> list1 = bookingDAO.findAll(example, sort);
//            for (Booking booking1 : list1) {
//                if (!booking1.getState().equalsIgnoreCase(delete)) {
//                    list.add(booking1);
//                }
//            }
//            int i = 0;
//            if (booking.getTimeSlot() == null || booking.getTimeSlot().getBookingDate() == null) {
//                for (i = start * size; i < (start + 1) * size && i < list.size(); i++) {//start=0 size=2
//                    Booking booking1 = list.get(i);
//                    res.add(booking1);
//                }
//                count = list.size();
//            } else {
//                List<TimeSlot> tsList = timeSlotService.findByBookingDate(booking.getTimeSlot().getBookingDate());
//                for (Booking booking1 : list) {
//                    TimeSlot timeSlot = booking1.getTimeSlot();
//                    for (TimeSlot ts : tsList) {
//                        if (timeSlot.getId() == ts.getId()) {
//                            if (count >= start * size && count < (start + 1) * size) {
//                                res.add(booking1);
//                            }
//                            count++;
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//
//        pageFromJPA = new PageImpl<Booking>(res, pageable, count);
//        return new Page4Navigator<>(pageFromJPA, navigatePages);
//    }
//
    // 参数appointvo的startDate-endDate代表要搜索的appoint的结束日期的范围
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult search(AppointVO appointVO, int start, int size) {
        PageHelper.startPage(start, size);
        List<AppointVO> list = appointMapperCustom.search(appointVO.getBookIsbn(),appointVO.getBookName(),appointVO.getUserName(),
                appointVO.getState());
        List<AppointVO> ans= new ArrayList<>();
        Date from = appointVO.getStartDate();
        Date to = appointVO.getEndDate();

        for(AppointVO appoint : list ){
            Date date = appoint.getEndDate();
            if(
                    (to==null||date.compareTo(to)<=0)
                    &&(from==null||date.compareTo(from)>=0)
            ){
                ans.add(appoint);
            }
        }
        PageInfo<AppointVO> pageList = new PageInfo<>(ans);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(start);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(ans);
        pagedResult.setRecords(pageList.getTotal());
        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult searchWithoutDelete(AppointVO appointVO, int start, int size) {
        PageHelper.startPage(start, size);
        List<AppointVO> list = appointMapperCustom.searchWithoutDelete(appointVO.getBookName(),appointVO.getUserId(),
                appointVO.getState());
        List<AppointVO> ans= new ArrayList<>();
        Date from = appointVO.getStartDate();
        Date to = appointVO.getEndDate();

        for(AppointVO appoint : list ){
            Date date = appoint.getEndDate();
            if(
                    (to==null||date.compareTo(to)<=0)
                            &&(from==null||date.compareTo(from)>=0)
            ){
                ans.add(appoint);
            }
        }
        PageInfo<AppointVO> pageList = new PageInfo<>(ans);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(start);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(ans);
        pagedResult.setRecords(pageList.getTotal());
        return pagedResult;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(Appoint appoint) {
        appointMapper.updateByPrimaryKeySelective(appoint);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Appoint get(int id) {
        return appointMapper.selectByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer total(Book book) {
        Example appointExample = new Example(Appoint.class);
        Criteria criteria = appointExample.createCriteria();
        criteria.andEqualTo("bookId", book.getId());
        String[] states = new String[]{cancelled, refused};
        criteria.andNotIn("state", Arrays.asList(states));
        return appointMapper.selectCountByExample(appointExample);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveAppoint(Appoint appoint) {
        appointMapper.insertUseGeneratedKeys(appoint);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<Appoint> findAll() {
        return appointMapper.selectAll();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean queryOrderIsExist(int userId, int bookId) {
        Example appointExample = new Example(Appoint.class);
        Criteria criteria = appointExample.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("bookId", bookId);
        String[] states = new String[]{waitTime, waitArrive, waitFinish};
        criteria.andIn("state", Arrays.asList(states));
        int count = appointMapper.selectCountByExample(appointExample);
        return count > 0;
    }

}