package com.meet.service;

import com.meet.pojo.User;
import com.meet.pojo.vo.UserVO;
import com.meet.utils.PagedResult;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;

public interface UserService {
    public static final String COOKI_NAME_TOKEN = "token";

    public User queryUsernameIsExist(String username);

    public void saveUser(User user);

    public User queryUserForLogin(String username, String password);

    public void updateUserInfo(User user);

    public User queryUserInfo(Integer userId);
    public UserVO addCookie(HttpServletResponse response, String token, User user);
    public User getByToken(HttpServletResponse response, String token);
    public PagedResult list( int start, int size);
    public PagedResult search(User user, int start, int size);
        public void logout(String userId);
}
