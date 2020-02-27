package com.meet.controller;

import com.meet.pojo.Admin;
import com.meet.pojo.Appoint;
import com.meet.pojo.User;
import com.meet.pojo.vo.AppointVO;
import com.meet.result.CodeMsg;
import com.meet.result.Result;
import com.meet.service.AppointService;
import com.meet.service.BookService;
import com.meet.service.UserService;
import com.meet.utils.PagedResult;
import com.meet.utils.RedisConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.print.Book;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
//@Api(value="订单的接口", tags= {"订单管理的controller"})
@RequestMapping("/appoint")
public class AppointController extends BasicController {

    @Autowired
    AppointService appointService;
    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;

    @GetMapping("/{id}")
    public Result<Appoint> getOne(@PathVariable("id") Integer id) {
        Appoint appoint = appointService.get(id);
        if(appoint==null)
            return Result.error(CodeMsg.REQUEST_ILLEGAL);

        return Result.success(appoint);
    }

    @GetMapping("")
    public Result<PagedResult> list(
            @RequestParam(value = "start", defaultValue = "1") Integer start,
            Integer size) {
        start = start < 1 ? 1 : start;
        if (size == null) size = PAGE_SIZE;
        PagedResult pagedResult = appointService.list(start, size);
        return Result.success(pagedResult);
    }

    // 参数appointvo的startDate-endDate代表要搜索的appoint的结束日期的范围
    @PostMapping("")
    public Result<PagedResult> search(Admin admin, @RequestBody AppointVO appointVO, Integer date,
                                      @RequestParam(value = "start", defaultValue = "1") Integer start,
                                      Integer size) {
        start = start < 1 ? 1 : start;
        if (size == null) size = PAGE_SIZE;
//        if (date != null) {
//            LocalDate today = LocalDate.now();
//            ZoneId zone = ZoneId.systemDefault();
//            LocalDate startDate = today.plusDays(date);
//            Instant instant = startDate.atStartOfDay().atZone(zone).toInstant();
//            appointVO.setStartDate(Date.from(instant));
//        }
        PagedResult pagedResult = appointService.search(appointVO, start, size);
        return Result.success(pagedResult);
    }

    //    @ApiOperation(value="查询我的订单", notes="用户查询我的订单的接口（不包括已删除的订单）")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="start", value="页码", required = true, dataType="Integer"),
//            @ApiImplicitParam(name="size", value="每页的显示个数",  dataType="Integer")
//    })

    // 参数appointvo的startDate-endDate代表要搜索的appoint的结束日期的范围
    @PostMapping("/mine")
    public Result<PagedResult> listMyBookings(User user, @RequestBody AppointVO appointVO,
                                              @RequestParam(value = "start", defaultValue = "1") Integer start,
                                              Integer size) {
        /**
         * 修改
         */
        Integer userId = user.getId();
        appointVO.setUserId(userId);
        start = start < 1 ? 1 : start;
        if (size == null) size = PAGE_SIZE;
        PagedResult pagedResult = appointService.search(appointVO, start, size);
        return Result.success(pagedResult);
    }


    @PutMapping("")
    public Result<Appoint> update(HttpServletRequest request,@RequestBody Appoint appoint) {
        Integer userId = isLogin(request,userService.COOKI_NAME_TOKEN,
                userService.USER_ID,RedisConstant.USER_LOGIN_REDIS_SESSION);
        if(userId ==null)
            return Result.error(CodeMsg.NOT_LOGIN);
        appointService.update(appoint);
        return Result.success(null);
    }

    @GetMapping("/makeOrder/{bid}")
    public Result<Appoint> add(HttpServletRequest request,
                               @PathVariable("bid") Integer bid, int date, String remark) {
        /**
         * 修改
         */
        Integer userId = isLogin(request,userService.COOKI_NAME_TOKEN,
                userService.USER_ID,RedisConstant.USER_LOGIN_REDIS_SESSION);
        if(userId ==null)
            return Result.error(CodeMsg.NOT_LOGIN);
        if (appointService.queryOrderIsExist(userId, bid))
            return Result.error(CodeMsg.BOOK_HAS_ORDERED);
        if (!bookService.reduceStock(bid))
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        Appoint appoint = new Appoint();
        appoint.setRemark(remark);
        appoint.setCreateDate(new Date());
        LocalDate today = LocalDate.now();
        ZoneId zone = ZoneId.systemDefault();
        LocalDate startDate = today.plusDays(date);
        Instant instant = startDate.atStartOfDay().atZone(zone).toInstant();
        appoint.setStartDate(Date.from(instant));
        LocalDate endDate = startDate.plusMonths(1);
        instant = endDate.atStartOfDay().atZone(zone).toInstant();
        appoint.setEndDate(Date.from(instant));
        if (date == 0)
            appoint.setState(appointService.waitArrive);
        else
            appoint.setState(appointService.waitTime);
        appoint.setUserId(userId);
        appoint.setBookId(bid);
        appointService.saveAppoint(appoint);
        return Result.success(appoint);
    }

    @PutMapping("/delete/{id}")
    public Result<Appoint> delete(@PathVariable("id") int id) {
        Appoint appoint = appointService.get(id);
        appoint.setState(AppointService.delete);
        appointService.update(appoint);
        return Result.success(appoint);
    }

    @PutMapping("/arrive/{id}")
    public Result<Appoint> arrive(Admin admin, @PathVariable("id") int id) {
        Appoint appoint = appointService.get(id);
        appoint.setState(AppointService.waitFinish);
        appoint.setArriveDate(new Date());
        appointService.update(appoint);
        return Result.success(appoint);
    }

    @PutMapping("/back/{id}")
    public Result<Appoint> back(Admin admin, @PathVariable("id") int id) {
        Appoint appoint = appointService.get(id);
        bookService.addStock(appoint.getBookId());
        appoint.setState(AppointService.waitReview);
        appoint.setBackDate(new Date());
        appointService.update(appoint);
        return Result.success(appoint);
    }

    @PutMapping("/cancel/{id}")
    public Result<Appoint> cancel(HttpServletRequest request,@PathVariable("id") int id) {
        Integer userId = isLogin(request,userService.COOKI_NAME_TOKEN,
                userService.USER_ID,RedisConstant.USER_LOGIN_REDIS_SESSION);
        if(userId ==null)
            return Result.error(CodeMsg.NOT_LOGIN);

        Appoint appoint = appointService.get(id);
        appoint.setState(AppointService.cancelled);
        appointService.update(appoint);
        return Result.success(appoint);
    }

    @PutMapping("/approve/{id}")
    public Result<Appoint> approve(Admin admin, @PathVariable("id") int id) {
        Appoint appoint = appointService.get(id);
        Date today = new Date();
        if(appoint.getStartDate().after(today))
            appoint.setState(AppointService.waitTime);
        else
            appoint.setState(AppointService.waitArrive);
        appointService.update(appoint);
        return Result.success(appoint);
    }

    @PutMapping("/refuse/{id}")
    public Result<Appoint> refuse(Admin admin, @PathVariable("id") int id) {
        Appoint appoint = appointService.get(id);
        appoint.setState(AppointService.refused);
        appointService.update(appoint);
        return Result.success(appoint);
    }

    @Scheduled(cron = "0 0 0 1/1 * ? ")//每天更新
    public void changeAppointState() {
        Date today = new Date();
        List<Appoint> appoints = appointService.findAllWithoutStatic();
        for (Appoint a : appoints) {
            Date start = a.getStartDate();
            Date end = a.getEndDate();
            String curState = a.getState();
            String nextState = "";
            if (start.compareTo(today) == 0 && curState.equals(AppointService.waitTime)) {
                nextState = AppointService.waitArrive;
            } else if (start.compareTo(today) < 0 && curState.equals(AppointService.waitArrive)) {
                nextState = AppointService.cancelled;
            } else if (end.compareTo(today) < 0 && curState.equals(AppointService.waitFinish)) {
                nextState = AppointService.late;
            } else {
                continue;
            }
            a.setState(nextState);
            appointService.update(a);
        }
    }
}
