package com.meet.controller;

import com.meet.pojo.User;
import com.meet.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ForePageController extends BasicController{
	@Autowired
	RedisOperator redis;
	@RequestMapping(value="/to_login")
	public String tologin(){
		return "login";
	}

	@GetMapping(value="/home")
	public String home(){
		return "fore/home";
	}
	@GetMapping(value="/register")
	public String register(){
		return "fore/register";
	}
	@GetMapping(value="/profile")
	public String profile(){
		return "fore/profile";
	}
	@GetMapping(value="/password")
	public String password(){
		return "fore/password";
	}
	@GetMapping(value="/new")
	public String thenew(){
		return "fore/new";
	}
	@GetMapping(value="/forenews")
	public String news(){
		return "fore/news";
	}
	@GetMapping(value="/to_book")
	public String venue(){
		return "fore/book";
	}
	@GetMapping(value="/forebooks")
	public String venues(){
		return "fore/books";
	}
	@GetMapping(value="/bought")
	public String bought(){
		return "fore/bought";
	}
	@GetMapping(value="/login")
	public String login(){
		return "fore/login";
	}
	@GetMapping(value="/review")
	public String review(){
		return "fore/review";
	}
	@GetMapping("/forelogout")
	public String logout(HttpSession session) {
		User user = (User) session.getAttribute("user");
		redis.del(USER_REDIS_SESSION + ":" + user.getId());
		session.removeAttribute("user");
		return "redirect:first";
	}

}

