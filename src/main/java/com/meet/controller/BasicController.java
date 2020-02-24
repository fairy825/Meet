package com.meet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BasicController {

//	@Autowired
//	public RedisOperator redis;

    public static final String USER_REDIS_SESSION = "user-redis-session";
    public static final String ADMIN_REDIS_SESSION = "admin-redis-session";
    public static final String START_TIME = "start-time";

    // 每页分页的记录数
    public static final Integer PAGE_SIZE = 2;
    public static final String FILE_SPACE = "D:/workspace/Meet/src/main/resources/static/img";


}
