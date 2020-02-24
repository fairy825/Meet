package com.meet.controller;

import com.meet.pojo.Appoint;
import com.meet.pojo.Comment;
import com.meet.pojo.User;
import com.meet.result.CodeMsg;
import com.meet.result.Result;
import com.meet.service.AppointService;
import com.meet.service.BookService;
import com.meet.service.CommentService;
import com.meet.utils.PagedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
//@Api(value="留言的接口", tags= {"留言管理的controller"})
public class CommentController extends BasicController {

    @Autowired
    CommentService commentService;
    @Autowired
    AppointService appointService;
    @Autowired
    BookService bookService;
    //    @Autowired
//    UserService userService;
//    @Autowired
//    VenueService venueService;
//    @Autowired
//    BookingService bookingService;
    @GetMapping("/book/{bid}/comment")
    public Result<PagedResult> list(@PathVariable("bid") int bid,
                                    @RequestParam(value = "status", defaultValue = "0") Integer status,
                                    @RequestParam(value = "start", defaultValue = "1") Integer start,
                                    Integer size) {
        start = start < 1 ? 1 : start;
        if (size == null) size = PAGE_SIZE;
        PagedResult pagedResult = commentService.listByBook(bid, status, start, size);
        return Result.success(pagedResult);
    }


    @GetMapping("/comment/mine")
    public Result<Comment> myreview(@RequestParam(value = "aid") Integer aid){
        Comment comment = commentService.queryByAppoint(aid);
        return Result.success(comment);
    }

    @GetMapping("/comment")
    public Result<PagedResult> list(@RequestParam(value = "start", defaultValue = "0") Integer start,
                                    Integer size){
        start = start < 1 ? 1 : start;
        if (size == null) size = PAGE_SIZE;
        PagedResult pagedResult = commentService.list(start, size);
        return Result.success(pagedResult);
    }
//
//    @ApiOperation(value = "对某一场馆留言评论", notes = "用户在服务结束后给场馆留言的接口")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "vid", value = "场馆id", required = true, dataType = "Integer"),
//            @ApiImplicitParam(name = "bid", value = "订单id", required = true, dataType = "Integer")
//    })
    @PostMapping("/comment/{bid}")
    public Result add(User user, @RequestBody Comment comment, @PathVariable("bid") int bid,
                      @RequestParam(value = "aid") int aid){
        Appoint appoint = appointService.get(aid);

        if (!appoint.getState().equalsIgnoreCase(appointService.waitReview))
            return Result.error(CodeMsg.HAS_REVIEWED);
        bookService.addRating(bid,comment.getRating());

        comment.setUserId(user.getId());
        comment.setBookId(bid);
        comment.setState(commentService.pass);
        comment.setCreateDate(new Date());
        comment.setAppointId(aid);
        commentService.saveComment(comment);
        appoint.setState(appointService.finish);
        appointService.update(appoint);
        return Result.success(comment);
    }

    @DeleteMapping("/comment/{id}")
    public Result suspend(@PathVariable("id") int id) {
        Integer appointId = commentService.get(id).getAppointId();
        Appoint appoint = appointService.get(appointId);
        appoint.setState(AppointService.waitReview);
        int rating = commentService.get(id).getRating();
        bookService.deleteRating(appoint.getBookId(),rating);

        commentService.delete(id);

        appointService.update(appoint);

        return Result.success(null);
    }

    @PutMapping("/comment/agree/{id}")
    public Result<Comment> agree(@PathVariable("id") int id) {

        Comment comment = commentService.get(id);
        comment.setState(commentService.pass);
        bookService.addRating(comment.getBookId(),comment.getRating());

        commentService.update(comment);
        return Result.success(comment);
    }

    @PutMapping("/comment/refuse/{id}")
    public Result<Comment> refuse(@PathVariable("id") int id) {

        Comment comment = commentService.get(id);
        comment.setState(commentService.refused);
        bookService.deleteRating(comment.getBookId(),comment.getRating());
        commentService.update(comment);
        return Result.success(comment);
    }
}
