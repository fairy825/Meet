package com.meet.controller;

import com.meet.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class BasicController {

	@Autowired
	public RedisOperator redis;

    // 每页分页的记录数
    public static final Integer PAGE_SIZE = 2;
    public static final String FILE_SPACE = "D:/workspace/Meet/src/main/resources/static/img";

    public Integer isLogin(HttpServletRequest request,
                            String onlineToken,String redisToken,String redisFile){
        String paramToken = request.getParameter(onlineToken);
        String userId = getCookieValue(request, redisToken);
        String cookieToken = getCookieValue(request, onlineToken);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;

        String byToken = getToken(redisFile+":"+userId);
        if(byToken==null||!byToken.equals(token)) {//redistoken==null
            return null;
        }
        return Integer.valueOf(userId);
    }
    private String getToken(String userId) {
        String token = redis.get(userId);
        return token;
    }
    private String getCookieValue(HttpServletRequest request, String cookiName) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(cookiName))
                return cookie.getValue();
        }
        return null;
    }
}
