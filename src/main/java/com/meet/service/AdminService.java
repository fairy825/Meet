package com.meet.service;

import com.meet.pojo.Admin;
import com.meet.pojo.User;
import com.meet.pojo.vo.AdminVO;

import javax.servlet.http.HttpServletResponse;

public interface AdminService {
    public static final String COOKI_NAME_TOKEN = "token";

    public Admin queryUserForLogin(String username, String password);
    public Admin queryUsernameIsExist(String username);
    public AdminVO addCookie(HttpServletResponse response, String token, Admin admin) ;
    public Admin getByToken(HttpServletResponse response, String token);

}