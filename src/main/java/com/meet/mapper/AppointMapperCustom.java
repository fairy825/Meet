package com.meet.mapper;

import com.meet.pojo.Appoint;
import com.meet.pojo.vo.AppointVO;
import com.meet.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface AppointMapperCustom extends MyMapper<AppointVO> {
    List<AppointVO> search(@Param("bookIsbn")String bookIsbn,@Param("bookName")String bookName,
                           @Param("userName")String userName, @Param("state")String state);
    List<AppointVO> searchWithoutDelete(@Param("bookName")String bookName, @Param("userId")Integer userId,
                           @Param("state")String state);
}