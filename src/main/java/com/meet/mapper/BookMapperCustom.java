package com.meet.mapper;

import com.meet.pojo.Book;
import com.meet.pojo.vo.BookVO;
import com.meet.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookMapperCustom extends MyMapper<Book> {
    public List<BookVO> queryByTitleLike(@Param("title") String title);
    public List<BookVO> queryByCategory(@Param("cid") Integer cid);
    public List<BookVO> search(@Param("cid") int cid, @Param("isbn") String isbn,
                             @Param("title") String title,
                             @Param("author") String author, @Param("publishing_house") String publishing_house,
                             @Param("stock") int stock, @Param("minRating") int minRating);
}