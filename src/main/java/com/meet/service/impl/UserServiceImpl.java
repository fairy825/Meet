package com.meet.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.meet.pojo.vo.UserVO;
import com.meet.result.CodeMsg;
import com.meet.result.Result;
import com.meet.service.UserService;
import com.meet.utils.PagedResult;
import com.meet.utils.RedisConstant;
import com.meet.utils.RedisOperator;
import com.meet.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.meet.mapper.UserMapper;
import com.meet.pojo.User;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisOperator redis;
//	@Autowired
//	private Sid sid;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User queryUsernameIsExist(String username) {
        //取缓存
        User result = redis.get(RedisConstant.USER_INFO_NAME + ":" + username, User.class);
        if (result != null) {
            return result;
        }
        //取数据库
        User user = new User();
        user.setName(username);
        result = userMapper.selectOne(user);
        if (result != null) {
            redis.setBean(RedisConstant.USER_INFO_NAME + ":" + username, result);
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(User user) {
        user.setNickname(user.getName());
        userMapper.insertUseGeneratedKeys(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User queryUserForLogin(String username, String password) {
        Example userExample = new Example(User.class);
        Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("name", username);
        criteria.andEqualTo("password", password);
        User result = userMapper.selectOneByExample(userExample);

        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserInfo(User user) {
        User curUser = queryUserInfo(user.getId());
        //取user
        //更新数据库
        Example userExample = new Example(User.class);
        Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id", user.getId());
        userMapper.updateByExampleSelective(user, userExample);
        //处理缓存
//        redis.del(RedisConstant.USER_INFO_ID+":"+curUser.getId());
//        redis.del(RedisConstant.USER_INFO_NAME+":"+curUser.getName());
//        BeanUtils.copyProperties(user,curUser);
        SpringUtil.copyPropertiesIgnoreNull(user, curUser);
        redis.setBean(RedisConstant.USER_INFO_ID + ":" + curUser.getId(), curUser);
        redis.setBean(RedisConstant.USER_INFO_NAME + ":" + curUser.getName(), curUser);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User queryUserInfo(Integer userId) {
        //取缓存
        User result = redis.get(RedisConstant.USER_INFO_ID + ":" + userId, User.class);
        if (result != null) {
            return result;
        }
        //取数据库
        Example userExample = new Example(User.class);
        Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id", userId);
        result = userMapper.selectOneByExample(userExample);
        if (result != null) {
            redis.setBean(RedisConstant.USER_INFO_ID + ":" + userId, result);
            redis.setBean(RedisConstant.USER_INFO_NAME + ":" + result.getName(), result);
        }
        return result;
    }

    @Override
    public String getToken(HttpServletResponse response, Integer userId) {
//        if (StringUtils.isEmpty(adminId)) {
//            return null;
//        }
//		MiaoshaUser user = getById(Long.parseLong(redis.get(MIAOSHA_USER_REDIS_SESSION + ":" + token)));
//        Admin admin = redis.get(RedisConstant.ADMIN_LOGIN_REDIS_SESSION + ":" + token, Admin.class);
        String token = redis.get(RedisConstant.USER_LOGIN_REDIS_SESSION + ":" + userId);
        // 延长有效期 有效期以最后一次登录的时间为准 所以每次操作都需要更新ttl
        if (userId != null && token != null) {
            addCookie(response, token, userId);
        }
        return token;
    }

    @Override
    public boolean addCookie(HttpServletResponse response, String token, Integer userId) {
        // 把token写到cookie当中 传给客户端
//		redis.set(MIAOSHA_USER_REDIS_SESSION + ":" + token, user.getId().toString(), 3600*24*2);
        redis.setBean(RedisConstant.USER_LOGIN_REDIS_SESSION + ":" + userId, token, 3600 * 24 * 2);
//		redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        Cookie cookie1 = new Cookie(USER_ID, String.valueOf(userId));
        cookie.setMaxAge((int) redis.ttl(RedisConstant.USER_LOGIN_REDIS_SESSION + ":" + userId));
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie1.setMaxAge((int) redis.ttl(RedisConstant.USER_LOGIN_REDIS_SESSION + ":" + userId));
        cookie1.setPath("/");
        response.addCookie(cookie1);

//        AdminVO adminVO = new AdminVO();
//        BeanUtils.copyProperties(admin, adminVO);
//        adminVO.setAdminToken(token);
        return true;
    }
//    @Override
//    public UserVO addCookie(HttpServletResponse response, String token, User user) {
//        // 把token写到cookie当中 传给客户端
////		redis.set(MIAOSHA_USER_REDIS_SESSION + ":" + token, user.getId().toString(), 3600*24*2);
//        redis.setBean(RedisConstant.USER_LOGIN_REDIS_SESSION + ":" + token, user, 3600 * 24 * 2);
////		redisService.set(MiaoshaUserKey.token, token, user);
//        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
//        cookie.setMaxAge((int) redis.ttl(RedisConstant.USER_LOGIN_REDIS_SESSION + ":" + token));
//        cookie.setPath("/");
//        response.addCookie(cookie);
//
//        UserVO userVO = new UserVO();
//        BeanUtils.copyProperties(user, userVO);
//        userVO.setUserToken(token);
//        return userVO;
//    }

    // 更新缓存时一定要先更新数据库再处理缓存
//    public boolean updatePassword(String token, Integer id, String formPass) {
//        // 取user
//        User user = queryUserInfo(id);
//        if (user == null) {
//            return false;
//            return Result.error(CodeMsg.MOBILE_NOT_EXIST);
//        }
//        // 更新数据库
//        User toBeUpdate = new User();
//        toBeUpdate.setId(id);
//        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
//        userMapper.updateByPrimaryKeySelective(toBeUpdate);
//        // 处理缓存
//        redis.del(RedisConstant.USER_INFO + ":" + id);
//        user.setPassword(toBeUpdate.getPassword());
//        redis.setBean(RedisConstant.USER_LOGIN_REDIS_SESSION + ":" + token, user);// 更新登录信息缓存
//        return true;
//    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult list(int start, int size) {
        PageHelper.startPage(start, size);
        List<User> list = userMapper.selectAll();

        PageInfo<User> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(start);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    // redis查询
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult search(User user, int start, int size) {
        PageHelper.startPage(start, size);
        Example userExample = new Example(User.class);
        Criteria criteria = userExample.createCriteria();
        if (user.getName() != null && !user.getName().equals("")) {
            criteria.andLike("name", "%" + user.getName() + "%");
        }
        if (user.getNickname() != null && !user.getNickname().equals("")) {
            criteria.andLike("nickname", "%" + user.getNickname() + "%");
        }

        List<User> list = userMapper.selectByExample(userExample);
        PageInfo<User> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(start);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

//    public User getByToken(HttpServletResponse response, String token) {
//        if (StringUtils.isEmpty(token)) {
//            return null;
//        }
////		MiaoshaUser user = getById(Long.parseLong(redis.get(MIAOSHA_USER_REDIS_SESSION + ":" + token)));
//        User user = redis.get(RedisConstant.USER_LOGIN_REDIS_SESSION + ":" + token, User.class);
//        // 延长有效期 有效期以最后一次登录的时间为准 所以每次操作都需要更新ttl
//        if (user != null) {
//            addCookie(response, token, user);
//        }
//        return user;
//    }

}
