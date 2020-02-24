package com.meet.controller;

import com.meet.pojo.Admin;
import com.meet.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

	@GetMapping(value="/alogout")
	public String adminLogout(HttpSession session){
		Admin admin = (Admin)session.getAttribute("admin");
		redis.del(ADMIN_REDIS_SESSION + ":" + admin.getId());
		session.removeAttribute("admin");
		return "redirect:first";
	}
	@GetMapping(value="/admin")
	public String admin(){
		return "redirect:admin_category_list";
	}

	@GetMapping(value="/admin_category_list")
	public String listDistrict(Admin admin, Model model){
		model.addAttribute("admin",admin);
		return "admin/listCategory";
	}

	@GetMapping(value="/admin_category_edit")
	public String editDistrict(){
		return "admin/editCategory";
	}
	@GetMapping(value="/admin_news_list")
	public String listNews(){
		return "admin/listNews";
	}
	@GetMapping(value="/admin_comment_list")
	public String listMessage(){
		return "admin/listComment";
	}

	@GetMapping(value="/admin_book_list")
	public String listVenue(){
		return "admin/listBook";
	}

	@GetMapping(value="/admin_book_edit")
	public String editVenue(){
		return "admin/editBook";
	}
	@GetMapping(value="/admin_bookimage_list")
	public String listVenueImage(){
		return "admin/listBookImage";
	}

	@GetMapping(value="/admin_user_list")
	public String listUser(){
		return "admin/listUser";
	}

	@GetMapping(value="/admin_appoint_list")
	public String listBooking(){
		return "admin/listAppoint";
	}
}
