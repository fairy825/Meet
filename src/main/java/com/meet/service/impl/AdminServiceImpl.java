package com.meet.service.impl;

import com.meet.mapper.AdminMapper;
import com.meet.pojo.Admin;
import com.meet.pojo.User;
import com.meet.pojo.vo.AdminVO;
import com.meet.service.AdminService;
import com.meet.service.UserService;
import com.meet.utils.RedisConstant;
import com.meet.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;
    @Autowired
    private RedisOperator redis;
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Admin queryUserForLogin(String username, String password){
        Example adminExample = new Example(Admin.class);
        Example.Criteria criteria = adminExample.createCriteria();
        criteria.andEqualTo("name", username);
        criteria.andEqualTo("password", password);
        Admin result = adminMapper.selectOneByExample(adminExample);

        return result;
    }
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Admin queryUsernameIsExist(String username){
        Admin admin = new Admin();
        admin.setName(username);

        Admin result = adminMapper.selectOne(admin);
        return result;
    }
    @Override
    public AdminVO addCookie(HttpServletResponse response, String token, Admin admin) {
        // 把token写到cookie当中 传给客户端
//		redis.set(MIAOSHA_USER_REDIS_SESSION + ":" + token, user.getId().toString(), 3600*24*2);
        redis.setBean(RedisConstant.ADMIN_LOGIN_REDIS_SESSION + ":" + token, admin, 3600 * 24 * 2);
//		redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge((int) redis.ttl(RedisConstant.ADMIN_LOGIN_REDIS_SESSION + ":" + token));
        cookie.setPath("/");
        response.addCookie(cookie);

        AdminVO adminVO = new AdminVO();
        BeanUtils.copyProperties(admin, adminVO);
        adminVO.setAdminToken(token);
        return adminVO;
    }
    @Override
    public Admin getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
//		MiaoshaUser user = getById(Long.parseLong(redis.get(MIAOSHA_USER_REDIS_SESSION + ":" + token)));
        Admin admin = redis.get(RedisConstant.ADMIN_LOGIN_REDIS_SESSION + ":" + token, Admin.class);
        // 延长有效期 有效期以最后一次登录的时间为准 所以每次操作都需要更新ttl
        if (admin != null) {
            addCookie(response, token, admin);
        }
        return admin;
    }
}