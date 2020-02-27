package com.meet.controller;

import com.meet.pojo.Admin;
import com.meet.pojo.Category;
import com.meet.result.Result;
import com.meet.service.CategoryService;
import com.meet.utils.PagedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController extends BasicController{
	@Autowired
    CategoryService categoryService;

    @GetMapping("")
    public Result<PagedResult> list(String keyword,
                                    @RequestParam(value = "start", defaultValue = "1") Integer start,
                                    Integer size)
            throws Exception {
        start = start<1?1:start;
        if(size == null) size = PAGE_SIZE;
        PagedResult pagedResult = categoryService.list(keyword,start,size);
        return Result.success(pagedResult);
    }
    @GetMapping("/all")
    public Result<List<Category>> listAll(){
        return Result.success(categoryService.findAll());
    }

    @PostMapping("")
    public Result<Category> add(Admin admin, @RequestBody Category category){
        categoryService.saveCategory(category);
        return Result.success(category);
    }

    @DeleteMapping("/{id}")
    public Result delete(Admin admin, @PathVariable("id") int id){
        categoryService.delete(id);
        return Result.success(null);
    }

    @GetMapping("/{id}")
    public Result<Category> get(@PathVariable("id") int id) {
        Category category=categoryService.get(id);
        return Result.success(category);
    }

    @PutMapping("")
    public Result update(Admin admin, @RequestBody  Category category) {
        categoryService.update(category);
        return Result.success(null);
    }

}
