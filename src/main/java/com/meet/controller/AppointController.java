package com.meet.controller;

import com.meet.pojo.Admin;
import com.meet.pojo.Appoint;
import com.meet.pojo.Book;
import com.meet.pojo.User;
import com.meet.pojo.vo.AppointVO;
import com.meet.rabbitmq.MQSender;
import com.meet.rabbitmq.MiaoshaMessage;
import com.meet.result.CodeMsg;
import com.meet.result.Result;
import com.meet.service.AppointService;
import com.meet.service.BookService;
import com.meet.service.UserService;
import com.meet.utils.PagedResult;
import com.meet.utils.RedisConstant;
import com.meet.utils.RedisOperator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@RestController
//@Api(value="订单的接口", tags= {"订单管理的controller"})
@RequestMapping("/appoint")
public class AppointController extends BasicController implements InitializingBean {

    @Autowired
    AppointService appointService;
    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;
    @Autowired
    MQSender sender;

    @GetMapping("/{id}")
    public Result<Appoint> getOne(@PathVariable("id") Integer id) {
        Appoint appoint = appointService.get(id);
        if (appoint == null)
            return Result.error(CodeMsg.REQUEST_ILLEGAL);

        return Result.success(appoint);
    }

    @GetMapping("/result/{bookId}")
    public Result<Integer> getAppointByBookId(User user, @PathVariable("bookId") Integer bookId) {
        if (user == null)
            return Result.error(CodeMsg.NOT_LOGIN);
        Integer result = appointService.getMiaoshaResult(user.getId(), bookId);
        return Result.success(result);
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
    public Result<Appoint> update(User user, HttpServletRequest request, @RequestBody Appoint appoint) {
//        Integer userId = isLogin(request,userService.COOKI_NAME_TOKEN,
//                userService.USER_ID,RedisConstant.USER_LOGIN_REDIS_SESSION);
        if (user == null)
            return Result.error(CodeMsg.NOT_LOGIN);
        appointService.update(appoint);
        return Result.success(null);
    }

    @GetMapping("/makeOrder/{path}/{bid}")
    public Result<Integer> add(User user, HttpServletRequest request,
                               @PathVariable("path") String path,
                               @PathVariable("bid") Integer bid, int date, String remark) {
//        Integer userId = isLogin(request,userService.COOKI_NAME_TOKEN,
//                userService.USER_ID,RedisConstant.USER_LOGIN_REDIS_SESSION);
        if (user == null)
            return Result.error(CodeMsg.NOT_LOGIN);
        //验证path
        boolean check = appointService.checkPath(user.getId(), bid, path);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //redis 预减库存
        long curStock = redis.decr(RedisConstant.BOOK_STOCK + ":" + bid, 1);
        if (curStock < 0)//最后变成redis=-1 sql=0
            return Result.error(CodeMsg.MIAOSHA_FAIL);

        //判断是否已经下单过
        if (appointService.queryOrderIsExist(user.getId(), bid))
            return Result.error(CodeMsg.BOOK_HAS_ORDERED);

        //入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setBookId(bid);
        mm.setDate(date);
        mm.setRemark(remark);
        sender.sendMessage(mm);
        return Result.success(0);//排队中

        //判断库存并下单
//        if (!bookService.reduceStock(bid))
//            return Result.error(CodeMsg.MIAOSHA_FAIL);
//        Appoint appoint = new Appoint();
//        appoint.setRemark(remark);
//        appoint.setCreateDate(new Date());
//        LocalDate today = LocalDate.now();
//        ZoneId zone = ZoneId.systemDefault();
//        LocalDate startDate = today.plusDays(date);
//        Instant instant = startDate.atStartOfDay().atZone(zone).toInstant();
//        appoint.setStartDate(Date.from(instant));
//        LocalDate endDate = startDate.plusMonths(1);
//        instant = endDate.atStartOfDay().atZone(zone).toInstant();
//        appoint.setEndDate(Date.from(instant));
//        if (date == 0)
//            appoint.setState(appointService.waitArrive);
//        else
//            appoint.setState(appointService.waitTime);
//        appoint.setUserId(user.getId());
//        appoint.setBookId(bid);
//        appointService.saveAppoint(appoint);
//        return Result.success(appoint);
    }

    @GetMapping(value = "/path")
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, User user,
                                         @RequestParam("bookId") int bookId
            , @RequestParam(value = "verifyCode", defaultValue = "0") int verifyCode) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        //检查执行次数
        String key = request.getRequestURI();

        key += "_" + user.getId();
        String ak = RedisConstant.ACCESS + ":" + key;
        Integer count = redis.get(ak, Integer.class);
        if (count == null) {
            redis.set(ak, String.valueOf(1),60);
        } else if (count < 5) {
            redis.incr(ak, 1);
        } else {
            return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
        }

        boolean check = appointService.checkVerifyCode(user.getId(), bookId, verifyCode);
        if (!check) {
            return Result.error(CodeMsg.WRONG_VERIFY_CODE);
        }
        String path = appointService.createMiaoshaPath(user.getId(), bookId);
        return Result.success(path);
    }

    @GetMapping(value = "/verifyCode")
    @ResponseBody
    public Result<String> getMiaoshaVerifyCode(HttpServletResponse response, User user,
                                               @RequestParam("bookId") Integer bookId) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        try {
            BufferedImage image = appointService.createVerifyCode(user.getId(), bookId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
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
    public Result<Appoint> cancel(User user, HttpServletRequest request, @PathVariable("id") int id) {
//        Integer userId = isLogin(request,userService.COOKI_NAME_TOKEN,
//                userService.USER_ID,RedisConstant.USER_LOGIN_REDIS_SESSION);
        if (user == null)
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
        if (appoint.getStartDate().after(today))
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

    @Override
    public void afterPropertiesSet() {
        List<Book> bookList = bookService.list();
        if (bookList == null) {
            return;
        }
        for (Book book : bookList) {
            redis.set(RedisConstant.BOOK_STOCK + ":" + book.getId(), String.valueOf(book.getStock()));
        }
    }
}
