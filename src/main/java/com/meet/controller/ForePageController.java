package com.meet.controller;

import com.meet.pojo.News;
import com.meet.pojo.User;
import com.meet.service.NewsService;
import com.meet.service.UserService;
import com.meet.utils.RedisConstant;
import com.meet.utils.RedisOperator;
import org.apache.catalina.core.ApplicationContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ForePageController extends BasicController{
	@Autowired
	RedisOperator redis;
	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;
//	@Autowired
//	ApplicationContext applicationContext;
	@Autowired
	NewsService newsService;

//	@GetMapping(value="/home")//
//	public String home(User user, Model model){
//		model.addAttribute("user",user);
//		return "fore/home";
//	}

	@GetMapping(value="/home",produces="text/html")
	@ResponseBody
	public String home(HttpServletRequest request, HttpServletResponse response, User user, Model model){
		//取缓存
		String html = redis.get(RedisConstant.HOMEPAGE, String.class);
		if(!StringUtils.isEmpty(html)) {
			return html;
		}
		//手动渲染
		model.addAttribute("user",user);
		List beans = newsService.list(null,1,5).getRows();
		model.addAttribute("beans",beans);

		WebContext ctx = new WebContext(request,response,
				request.getServletContext(),request.getLocale(), model.asMap());
		html = thymeleafViewResolver.getTemplateEngine().process("fore/home", ctx);
		if(!StringUtils.isEmpty(html)) {
			redis.set(RedisConstant.HOMEPAGE, html,60);
		}
		return html;
	}

	@GetMapping(value="/register")//
	public String register(){
		return "fore/register";
	}
	@GetMapping(value="/login")//
	public String login(){
		return "fore/login";
	}
	@GetMapping(value="/profile")
	public String profile(User user, Model model){
		model.addAttribute("user",user);
		return "fore/profile";
	}
	@GetMapping(value="/password")
	public String password(User user, Model model){
		model.addAttribute("user",user);
		return "fore/password";
	}
	@GetMapping(value="/new")//
	public String thenew(User user, Model model){
		model.addAttribute("user",user);
		return "fore/new";
	}
	@GetMapping(value="/forenews")//
	public String news(User user, Model model){
		model.addAttribute("user",user);
		return "fore/news";
	}
	@GetMapping(value="/to_book")//
	public String venue(User user, Model model){
		model.addAttribute("user",user);
		return "fore/book";
	}
	@GetMapping(value="/forebooks")//
	public String venues(User user, Model model){
		model.addAttribute("user",user);
		return "fore/books";
	}
	@GetMapping(value="/bought")
	public String bought(User user, Model model){
		model.addAttribute("user",user);
		return "fore/bought";
	}
	@GetMapping(value="/review")
	public String review(User user, Model model){
		model.addAttribute("user",user);
		return "fore/review";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		Integer userId = 0;
		for(Cookie cookie:cookies){
			if(cookie.getName().equals(UserService.USER_ID)){
				userId = Integer.valueOf(cookie.getValue());
				cookie.setMaxAge(0);
			} else if(cookie.getName().equals(UserService.COOKI_NAME_TOKEN)){
				cookie.setMaxAge(0);
			}
		}
		redis.del(RedisConstant.USER_LOGIN_REDIS_SESSION + ":" + userId);
		return "redirect:first";
	}
}

