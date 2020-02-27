package com.meet.controller;

import com.meet.pojo.Admin;
import com.meet.pojo.Book;
import com.meet.pojo.Category;
import com.meet.pojo.bo.BookBO;
import com.meet.pojo.vo.BookVO;
import com.meet.result.Result;
import com.meet.service.BookService;
import com.meet.service.BookimageService;
import com.meet.service.CategoryService;
import com.meet.utils.PagedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
//@Api(value="场馆的接口", tags= {"场馆管理的controller"})
public class BookController extends BasicController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    BookService bookService;
    @Autowired
    BookimageService bookimageService;
//    @Autowired
//    MessageService messageService;
//    @Autowired
//    VenueImageService venueImageService;

    @GetMapping("/category/{cid}/book")
    public Result<PagedResult> list(@PathVariable("cid") int cid, @RequestParam(value = "start", defaultValue = "1") Integer start,
                                    Integer size)
            throws Exception {
        start = start < 1 ? 1 : start;
        if (size == null) size = PAGE_SIZE;
        PagedResult pagedResult = bookService.listByCategory(cid, start, size);
        return Result.success(pagedResult);
    }

    @PostMapping("/book/search")
    public Result<PagedResult> list(@RequestBody BookVO bookVO,
                                    @RequestParam(value = "stock", defaultValue = "0") Integer stock,
                                    @RequestParam(value = "minRating", defaultValue = "0") Integer minRating,
                                    @RequestParam(value = "sort", defaultValue = "all") String sort,
                                    @RequestParam(value = "start", defaultValue = "1") Integer start,
                                    Integer size) {
        start = start < 1 ? 1 : start;
        if (size == null) size = PAGE_SIZE;
        PagedResult pagedResult = bookService.search(null, bookVO, stock, minRating, sort, start, size);
        return Result.success(pagedResult);
    }

    //搜索
    @PostMapping("/category/{cid}/book/search")
    public Result<PagedResult> searchFromAdmin(
            @PathVariable("cid") int cid, @RequestBody BookVO bookVO,
            @RequestParam(value = "stock", defaultValue = "0") Integer stock,
            @RequestParam(value = "minRating", defaultValue = "0") Integer minRating,
            @RequestParam(value = "sort", defaultValue = "all") String sort,
            @RequestParam(value = "start", defaultValue = "1") Integer start,
            Integer size) {
        start = start < 1 ? 1 : start;
        if (size == null) size = PAGE_SIZE;
        PagedResult pagedResult = bookService.search(cid, bookVO, stock, minRating, sort, start, size);
        return Result.success(pagedResult);
    }

    @PostMapping("/book")
    public Result<Book> add(Admin admin, @RequestBody Book book) {
//        venue.setDistrict(districtService.get(did));
        book.setUpdatedTime(new Date());
        bookService.saveBook(book);
        return Result.success(book);
    }

    @DeleteMapping("/book/{id}")
    public Result delete(Admin admin, @PathVariable("id") int id) {
        bookService.delete(id);
        return Result.success(null);
    }

    @GetMapping("/book/{id}")
    public Result<Book> get(@PathVariable("id") int id) {
        Book book = bookService.get(id);
        bookService.setSaleAndReviewNumber(book);
        bookimageService.setFirstBookimage(book);
        return Result.success(book);
    }

    @PutMapping("/book")
    public Result update(Admin admin, @RequestBody BookBO bookBO) {
        bookService.update(bookBO);
        return Result.success(null);
    }

}