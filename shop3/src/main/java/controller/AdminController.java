package controller;


import java.util.List;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import Exception.LoginException;
import logic.Item;
import logic.Sale;
import logic.SaleItem;
import logic.ShopService;
import logic.User;



@Controller   
@RequestMapping("admin")  
public class AdminController {
	
	@Autowired
	private ShopService service;


	@RequestMapping("list")   
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		List<User> userList = service.userList();
		mav.addObject("list",userList);
		return mav;
	}
}