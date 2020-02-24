package com.meet.mapper;

import com.meet.pojo.Comment;
import com.meet.pojo.vo.AppointVO;
import com.meet.pojo.vo.CommentVO;
import com.meet.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapperCustom extends MyMapper<CommentVO> {
    List<CommentVO> listByBook(@Param("bookId")Integer bookId, @Param("state")Integer state);
}