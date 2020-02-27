package com.meet.service;

import com.meet.pojo.Admin;
import com.meet.pojo.User;
import com.meet.pojo.vo.AdminVO;

import javax.servlet.http.HttpServletResponse;

public interface AdminService {
    public static final String COOKI_NAME_TOKEN = "admin-token";
    public static final String ADMIN_ID = "adminId";

    public Admin queryAdminInfo(Integer adminId);
    public Admin queryUserForLogin(String username, String password);
    public Admin queryUsernameIsExist(String username);
    public boolean addCookie(HttpServletResponse response, String token, Integer adminId) ;
    public String getToken(HttpServletResponse response,Integer adminId);

}