package com.meet.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.meet.pojo.vo.UserVO;
import com.meet.service.UserService;
import com.meet.utils.PagedResult;
import com.meet.utils.RedisConstant;
import com.meet.utils.RedisOperator;
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
        User user = new User();
        user.setName(username);
        User result = userMapper.selectOne(user);
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
        Example userExample = new Example(User.class);
        Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id", user.getId());
        userMapper.updateByExampleSelective(user, userExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User queryUserInfo(Integer userId) {
        Example userExample = new Example(User.class);
        Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id", userId);
        User result = userMapper.selectOneByExample(userExample);
        return result;
    }

    @Override
    public UserVO addCookie(HttpServletResponse response, String token, User user) {
        // 把token写到cookie当中 传给客户端
//		redis.set(MIAOSHA_USER_REDIS_SESSION + ":" + token, user.getId().toString(), 3600*24*2);
        redis.setBean(RedisConstant.USER_LOGIN_REDIS_SESSION + ":" + token, user, 3600 * 24 * 2);
//		redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge((int) redis.ttl(RedisConstant.USER_LOGIN_REDIS_SESSION + ":" + token));
        cookie.setPath("/");
        response.addCookie(cookie);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserToken(token);
        return userVO;
    }

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

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult search(User user, int start, int size) {
        PageHelper.startPage(start, size);

        Example userExample = new Example(User.class);
        Criteria criteria = userExample.createCriteria();
        if (user.getName() != null && !user.getName().equals(""))
            criteria.andLike("name", "%"+user.getName()+"%");
        if (user.getNickname() != null && !user.getNickname().equals(""))
            criteria.andLike("nickname", "%"+user.getNickname()+"%");
        List<User> list = userMapper.selectByExample(userExample);
        PageInfo<User> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(start);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    public User getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
//		MiaoshaUser user = getById(Long.parseLong(redis.get(MIAOSHA_USER_REDIS_SESSION + ":" + token)));
        User user = redis.get(RedisConstant.USER_LOGIN_REDIS_SESSION + ":" + token, User.class);
        // 延长有效期 有效期以最后一次登录的时间为准 所以每次操作都需要更新ttl
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

    @Override
    public void logout(String userId) {
//        redis.del();
    }

}
