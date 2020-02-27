package com.meet.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.meet.pojo.User;
import com.meet.pojo.vo.LoginVO;
import com.meet.pojo.vo.UserVO;
import com.meet.result.CodeMsg;
import com.meet.result.Result;
import com.meet.service.UserService;
import com.meet.utils.ImageUtil;
import com.meet.utils.MD5Util;
import com.meet.utils.PagedResult;
import com.sun.tools.javac.jvm.Code;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
//@Api(value="用户相关业务的接口", tags= {"用户相关业务的controller"})
@RequestMapping("/user")
public class UserController extends BasicController{
	
	@Autowired
	UserService userService;
	
//	@ApiOperation(value="查询用户信息", notes="查询用户信息的接口")
//	@ApiImplicitParam(name="userId", value="用户id", required=true,
//	dataType="String", paramType="query")
	@GetMapping("/profile")
	public Result<UserVO> query(User userInfo){
//		User userInfo = userService.queryUserInfo(userId);
		UserVO userVO = new UserVO();
		BeanUtils.copyProperties(userInfo, userVO);
	    
		return Result.success(userVO);
	}

	@GetMapping("")
	public Result<PagedResult> list(@RequestParam(value = "start", defaultValue = "1") Integer start,
								Integer size) {
		start = start<1?1:start;
		if(size == null) size = PAGE_SIZE;
		PagedResult pagedResult = userService.list(start,size);
		return Result.success(pagedResult);
	}
//	@ApiOperation(value="查找用户", notes="管理员根据条件查找用户的接口")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name="start", value="页码", required = true, dataType="Integer"),
//			@ApiImplicitParam(name="size", value="每页的显示个数",  dataType="Integer")
//	})
	@PostMapping("")
	public Result search(@RequestBody User user, @RequestParam(value = "start", defaultValue = "1") Integer start,
								  Integer size){
		start = start<1?1:start;
		if(size == null) size = PAGE_SIZE;
		PagedResult pagedResult = userService.search(user,start,size);
		return Result.success(pagedResult);
	}

//	@ApiOperation(value="更新用户个人信息", notes="用户更新自己的个人信息的接口")
	@PutMapping("/profile")
	public Result update(User loginUser,@RequestBody User user) {
//		User user1 = (User)session.getAttribute("user");
//
//		if(user1 == null)
//			return IMoocJSONResult.build(501,"未登录",null);
		userService.updateUserInfo(user);
		return Result.success(null);
	}

	//    @ApiOperation(value="更新用户密码", notes="用户修改密码的接口")

	@PutMapping("/password")
	public Result changePassword(User user, @RequestBody LoginVO loginVO, @RequestParam(value = "newPassword")String newPassword) throws Exception {
		String formPassword = loginVO.getPassword();
//		User user1 = userService.queryUserInfo(user.getId());
		if (StringUtils.isBlank(formPassword)) {
			return Result.error(CodeMsg.ORIGIN_PASSWORD_EMPTY);
		}
		if (StringUtils.isBlank(newPassword)) {
			return Result.error(CodeMsg.NEW_PASSWORD_EMPTY);
		}
//		userService.updatePassword(String token, Integer id, newPassword)
		if(userService.queryUserForLogin(user.getName(), MD5Util.formPassToDBPass(formPassword,user.getSalt()))!=null) {
			String newDbPass = MD5Util.formPassToDBPass(newPassword,user.getSalt());
			user.setPassword(newDbPass);
			userService.updateUserInfo(user);
			UserVO userVO = new UserVO();
			BeanUtils.copyProperties(user, userVO);
			return Result.success(userVO);
		}else{
			return Result.error(CodeMsg.PASSWORD_ERROR);
		}
	}

	@PostMapping("/upload")
	public Result upload(User user, @RequestParam("image") MultipartFile image){
		String folder = FILE_SPACE + "/faceImage";
		String fName = image.getOriginalFilename();
		String path = folder + "/" + user.getId()+".jpg";
		String pathDB =  "faceImage/" + user.getId()+".jpg";
//    String path = folder + "/" + fName;
//    String pathDB =  "img/faceImage/" + fName;
		User userDB = new User();
		BeanUtils.copyProperties(user,userDB);
		userDB.setFaceImage(pathDB);
		userService.updateUserInfo(userDB);
		System.out.println(path);
		System.out.println(pathDB);
		File file = new File(path);
		String fileName = file.getName();
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try {
			image.transferTo(file);
			BufferedImage img = ImageUtil.change2jpg(file);
			ImageIO.write(img, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String imageFolder_small= FILE_SPACE+"/faceImage_small";
		File f_small = new File(imageFolder_small, fileName);
		f_small.getParentFile().mkdirs();
		ImageUtil.resizeImage(file, 56, 56, f_small);
		return Result.success("img/"+pathDB);
	}
}
