package com.meet.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meet.pojo.User;
import com.meet.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 拦截器先执行
 * 关于参数的ArgumentResolver后执行
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();//获取参数类型
        return clazz == User.class;
    }

    //若是参数是user类型 则执行下列函数
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //用户登录 生成随机token 并存入redis
        //把token写到cookie当中 客户端持有cookie
        //页面跳转时携带cookie
        //根据cookie中的token 服务端可以到redis中取到userid 识别对应user

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

        String paramToken = request.getParameter(UserService.COOKI_NAME_TOKEN);
        String cookieToken = getCookieValue(request, UserService.COOKI_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken))
            return "login";
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        return userService.getByToken(response, token);
//		return UserContext.getUser();
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
