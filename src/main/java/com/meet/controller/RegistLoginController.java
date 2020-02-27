package com.meet.controller;

import com.meet.pojo.Admin;
import com.meet.pojo.User;
import com.meet.pojo.vo.AdminVO;
import com.meet.pojo.vo.LoginVO;
import com.meet.pojo.vo.UserVO;
import com.meet.result.CodeMsg;
import com.meet.result.Result;
import com.meet.service.AdminService;
import com.meet.service.UserService;
import com.meet.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class RegistLoginController extends BasicController{
	private static Logger log = LoggerFactory.getLogger(RegistLoginController.class);

	@Autowired
	UserService userService;
	@Autowired
	AdminService adminService;
	@Autowired
	RedisOperator redis;

	@PostMapping("/regist")
	public Result<UserVO> regist(HttpServletResponse response, @RequestBody LoginVO loginVo){
		String mobile = loginVo.getName();
		/**
		 * 修改
		 */
		String password = loginVo.getPassword();
		if(StringUtils.isBlank(mobile) || StringUtils.isBlank(password)) {
			return Result.error(CodeMsg.PASSWORD_EMPTY);
		}
		if(!ValidatorUtil.isMobile(mobile))
			return Result.error(CodeMsg.MOBILE_ERROR);
		String salt = "1a2b3c";
		User usernameIsExist = userService.queryUsernameIsExist(loginVo.getName());
		User user = new User();
		if(usernameIsExist==null) {
			user.setSalt(salt);
			user.setName(loginVo.getName());
			user.setNickname(loginVo.getName());
			user.setPassword(MD5Util.formPassToDBPass(password,salt));
			userService.saveUser(user);
		}
		else {
			return Result.error(CodeMsg.MOBILE_EXIST);
		}
		String token = UUIDUtil.uuid();
		userService.addCookie(response, token, user.getId());
		return Result.success(null);
	}
//	public UsersVO setUserRedisSessionToken(Users userModel) {
//		String uniqueToken = UUID.randomUUID().toString();
//		redis.set(USER_REDIS_SESSION + ":" + userModel.getId(), uniqueToken, 1000 * 60 * 30);
//
//		UsersVO userVO = new UsersVO();
//		BeanUtils.copyProperties(userModel, userVO);
//		userVO.setUserToken(uniqueToken);
//		return userVO;
//	}

	@PostMapping("/login")
	public Result<UserVO> login( @RequestBody LoginVO loginVo , HttpServletResponse response) {
		log.info(loginVo.toString());
		// 登录
		if (loginVo == null) {
			return Result.error(CodeMsg.SERVER_ERROR);
		}
		String mobile = loginVo.getName();
		if(!ValidatorUtil.isMobile(mobile))
			return Result.error(CodeMsg.MOBILE_ERROR);
		String formPass = loginVo.getPassword();
		if(StringUtils.isBlank(mobile)||StringUtils.isBlank(formPass))
			return Result.error(CodeMsg.PASSWORD_EMPTY);
		//判断用户名是否存在
		User user = userService.queryUsernameIsExist(mobile);
		if (user == null) {
			return Result.error(CodeMsg.MOBILE_NOT_EXIST);
		}
		//验证密码
		String dbPass = user.getPassword();
		String saltDB = user.getSalt();
		String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
		/**
		 * 修改
		 */
		if (!calcPass.equals(dbPass)) {
			return Result.error(CodeMsg.PASSWORD_ERROR);
		}
		//生成cookie
		String token = UUIDUtil.uuid();
		userService.addCookie(response, token, user.getId());

		return Result.success(null);
	}

	@PostMapping("/loginAdmin")
	public Result adminLogin(HttpServletResponse response, @RequestBody LoginVO loginVo) {
		log.info(loginVo.toString());

		String username = loginVo.getName();
		String password = loginVo.getPassword();
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return Result.error(CodeMsg.PASSWORD_EMPTY);
		}
		Admin admin = adminService.queryUsernameIsExist(username);
		if (admin!=null) {
			//验证密码
			String dbPass = admin.getPassword();
			if (dbPass.equals(password)) {
				//生成cookie
				String token = UUIDUtil.uuid();
				adminService.addCookie(response, token, admin.getId());
				return Result.success(null);
			}else{
			return Result.error(CodeMsg.PASSWORD_ERROR);
			}
		}
		else {
			return Result.error(CodeMsg.MOBILE_NOT_EXIST);
		}
	}
}
