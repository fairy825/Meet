package com.meet.controller;

import com.meet.pojo.Admin;
import com.meet.service.AdminService;
import com.meet.utils.RedisConstant;
import com.meet.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class AdminPageController extends BasicController{
	@Autowired
	RedisOperator redis;

	@GetMapping(value="/")
	public String index(){
		return "redirect:first";
	}
	@GetMapping(value="/first")
	public String first(){
		return "first";
	}
	@GetMapping(value="/alogin")
	public String adminLogin(){
		return "admin/adminLogin";
	}

	@GetMapping(value="/admin")
	public String admin(Admin admin, Model model){
		model.addAttribute("admin",admin);
		return "redirect:admin_category_list";
	}

	@GetMapping(value="/admin_category_list")
	public String listDistrict(Admin admin, Model model){
		model.addAttribute("admin",admin);
		return "admin/listCategory";
	}

	@GetMapping(value="/admin_category_edit")
	public String editDistrict(Admin admin, Model model){
		model.addAttribute("admin",admin);
		return "admin/editCategory";
	}
	@GetMapping(value="/admin_news_list")
	public String listNews(Admin admin, Model model){
		model.addAttribute("admin",admin);
		return "admin/listNews";
	}
	@GetMapping(value="/admin_comment_list")
	public String listMessage(Admin admin, Model model){
		model.addAttribute("admin",admin);
		return "admin/listComment";
	}

	@GetMapping(value="/admin_book_list")
	public String listVenue(Admin admin, Model model){
		model.addAttribute("admin",admin);
		return "admin/listBook";
	}

	@GetMapping(value="/admin_book_edit")
	public String editVenue(Admin admin, Model model){
		model.addAttribute("admin",admin);
		return "admin/editBook";
	}
	@GetMapping(value="/admin_bookimage_list")
	public String listVenueImage(Admin admin, Model model){
		model.addAttribute("admin",admin);
		return "admin/listBookImage";
	}

	@GetMapping(value="/admin_user_list")
	public String listUser(Admin admin, Model model){
		model.addAttribute("admin",admin);
		return "admin/listUser";
	}

	@GetMapping(value="/admin_appoint_list")
	public String listBooking(Admin admin, Model model){
		model.addAttribute("admin",admin);
		return "admin/listAppoint";
	}
}
