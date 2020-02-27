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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
        List<AppointVO> list = appointMapperCustom.search(null,null,null,null,
                null,null, null);
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

    // 参数appointvo的startDate-endDate代表要搜索的appoint的结束日期的范围
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult search(AppointVO appointVO, int start, int size) {
        Date from = appointVO.getStartDate();
        Date to = appointVO.getEndDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = "";
        String endDate = "";
       if(from!=null){
            startDate = sdf.format(from);
        }else
            startDate = "1900-01-01";
        if(to!=null){
            endDate = sdf.format(to);
        }else {
            LocalDate today = LocalDate.now();
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = today.plusMonths(1).atStartOfDay().atZone(zone).toInstant();
            endDate = sdf.format(Date.from(instant));
        }
        PageHelper.startPage(start, size);
        List<AppointVO> ans = appointMapperCustom.search(appointVO.getBookIsbn(),appointVO.getBookName(),
                appointVO.getUserId(),appointVO.getUserName(), appointVO.getState(),
                startDate,endDate);
//        List<AppointVO> ans= new ArrayList<>();
//        Date from = appointVO.getStartDate();
//        Date to = appointVO.getEndDate();

//        for(AppointVO appoint : list ){
//            Date date = appoint.getEndDate();
//            if(
//                    (to==null||date.compareTo(to)<=0)
//                    &&(from==null||date.compareTo(from)>=0)
//            ){
//                ans.add(appoint);
//            }
//        }
        PageInfo<AppointVO> pageList = new PageInfo<>(ans);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(start);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(ans);
        pagedResult.setRecords(pageList.getTotal());
        return pagedResult;
    }

//    @Transactional(propagation = Propagation.SUPPORTS)
//    @Override
//    public PagedResult searchWithoutDelete(AppointVO appointVO, int start, int size) {
//        List<AppointVO> list = appointMapperCustom.search(appointVO.getBookIsbn(),appointVO.getBookName(),appointVO.getUserId(),
//                appointVO.getState());
//        List<AppointVO> ans= new ArrayList<>();
//        Date from = appointVO.getStartDate();
//        Date to = appointVO.getEndDate();
//
//        for(AppointVO appoint : list ){
//            Date date = appoint.getEndDate();
//            if(
//                    (to==null||date.compareTo(to)<=0)
//                            &&(from==null||date.compareTo(from)>=0)
//            ){
//                ans.add(appoint);
//            }
//        }
//        PageHelper.startPage(start, size);
//        PageInfo<AppointVO> pageList = new PageInfo<>(ans);
//        PagedResult pagedResult = new PagedResult();
//        pagedResult.setPage(start);
//        pagedResult.setTotal(pageList.getPages());
//        pagedResult.setRows(ans);
//        pagedResult.setRecords(pageList.getTotal());
//        return pagedResult;
//    }

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