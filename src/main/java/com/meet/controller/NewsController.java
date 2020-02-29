package com.meet.controller;

import com.meet.pojo.Admin;
import com.meet.pojo.News;
import com.meet.result.Result;
import com.meet.service.NewsService;
import com.meet.utils.PagedResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
//@Api(value="新闻的接口", tags= {"新闻管理的controller"})
@RequestMapping("/news")
public class NewsController extends BasicController{

    @Autowired
    NewsService newsService;
//    @ApiOperation(value="查询新闻", notes="管理员或用户根据条件查询新闻的接口")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="keyword", value="搜索关键词", dataType="String"),
//            @ApiImplicitParam(name="start", value="页码", required = true, dataType="Integer"),
//            @ApiImplicitParam(name="size", value="每页的显示个数",  dataType="Integer")
//    })


    @GetMapping("")
    public Result<PagedResult> list(String keyword,
                                    @RequestParam(value = "start", defaultValue = "1") Integer start,
                                    Integer size){
    	start = start<1?1:start;
    	if(size == null) size = PAGE_SIZE;
        PagedResult pagedResult = newsService.list(keyword,start,size);
        return Result.success(pagedResult);
    }
//    @ApiOperation(value="新增新闻", notes="管理员新增新闻的接口")
    @PostMapping("")
    public Result add(Admin admin, @RequestBody News news) {
        newsService.saveNews(news);
        return Result.success(news);
    }
    @DeleteMapping("/{id}")
    public Result delete(Admin admin, @PathVariable("id") int id) {
        newsService.delete(id);
        return Result.success(null);
    }

    @GetMapping("/{id}")
    public Result<News> get(@PathVariable("id") int id){
        News news=newsService.get(id);
        return Result.success(news);
    }

}