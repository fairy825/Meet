package com.meet.mapper;

import com.meet.pojo.Book;
import com.meet.pojo.News;
import com.meet.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookMapper extends MyMapper<Book> {
    public List<Book> queryByTitleLike(@Param("title")String title);
    public List<Book> queryByCategory(@Param("cid")Integer cid);
    public List<Book> searchByCid( @Param("cid")int cid,  @Param("isbn")String isbn, @Param("title")String title,
                              @Param("author")String author,  @Param("publishing_house")String publishing_house,
                             @Param("stock")int stock, @Param("minRating")int minRating);
    public List<Book> search(  @Param("isbn")String isbn, @Param("title")String title,
                                   @Param("author")String author,  @Param("publishing_house")String publishing_house,
                                   @Param("stock")int stock, @Param("minRating")int minRating);
    public void reduceStock(@Param("id")Integer id);
    public void addStock(@Param("id")Integer id);
    public void deleteCategory(@Param("id")Integer id);
    public void updateCategory(@Param("bid")Integer bid, @Param("cid")Integer cid);
}