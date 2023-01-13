package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home Controller
 */
@Controller
public class HomeController{

	/**
	 * @param model
	 * @return show homePage
	 */
	@RequestMapping("/")
	public String home(Model model)
	{
		return "home";
	}

	/**
	 * @param model
	 * @return show admin home
	 */
	@RequestMapping("/admin/home")
	public String adminHome(Model model){
		return "redirect:/home";
	}


}
