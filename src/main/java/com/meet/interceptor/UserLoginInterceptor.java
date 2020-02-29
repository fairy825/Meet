package com.meet.interceptor;

import com.meet.service.UserService;
import com.meet.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class UserLoginInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Autowired
    public RedisOperator redis;

	@Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String paramToken = httpServletRequest.getParameter(UserService.COOKI_NAME_TOKEN);
        String userId = getCookieValue(httpServletRequest, UserService.USER_ID);
        String cookieToken = getCookieValue(httpServletRequest, UserService.COOKI_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            httpServletResponse.sendRedirect("login");
            return false;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;

        String byToken = userService.getToken(httpServletResponse, Integer.valueOf(userId));
        if(byToken==null) {//redistoken==null
            httpServletResponse.sendRedirect("login");
            return false;
        }else if(byToken.equals(token)){
            return true;
        }else{//账号被挤出
            httpServletResponse.sendRedirect("login");
            return false;
        }
    }
    private String getCookieValue(HttpServletRequest request, String cookiName) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(cookiName))
                return cookie.getValue();
        }
        return null;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
