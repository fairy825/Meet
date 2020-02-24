package com.meet.controller;

import com.meet.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HelloWorldController  extends BasicController{
	
	@RequestMapping("/hello")
	public String Hello(Model model, User user) {
		model.addAttribute("user",user);
		return "hello";
	}
	@RequestMapping("/hello2")
	public String Hello2(Model model) {
		return "hello2";
	}
//	@RequestMapping("/to_login")
//	public String tologin() {
//		return "login";
//	}
}
