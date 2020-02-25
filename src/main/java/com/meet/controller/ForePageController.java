package com.meet.controller;

import com.meet.pojo.User;
import com.meet.utils.RedisConstant;
import com.meet.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ForePageController extends BasicController{

	@GetMapping(value="/home")
	public String home(User user, Model model){
		model.addAttribute("user",user);
		return "fore/home";
	}
	@GetMapping(value="/register")
	public String register(){
		return "fore/register";
	}
	@GetMapping(value="/login")
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
	@GetMapping(value="/new")
	public String thenew(User user, Model model){
		model.addAttribute("user",user);
		return "fore/new";
	}
	@GetMapping(value="/forenews")
	public String news(User user, Model model){
		model.addAttribute("user",user);
		return "fore/news";
	}
	@GetMapping(value="/to_book")
	public String venue(User user, Model model){
		model.addAttribute("user",user);
		return "fore/book";
	}
	@GetMapping(value="/forebooks")
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


}

