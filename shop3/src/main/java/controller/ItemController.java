package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

@Controller   //@Compoent + controller (요청을 받을 수 있는 객체)   객체화시키고 기능을 추가
@RequestMapping("item")  // item/ 요청시    -> 그래서 http://localhost:8080/shop3/item/list.shop 을 접속할때 /item먼저 해야함
public class ItemController {
	
	@Autowired
	private ShopService service;
	
	@RequestMapping("list")   // item/list.shop
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		List<Item> itemList = service.getItemList();
		mav.addObject("itemList",itemList);
		return mav;
	}
	

	
	@RequestMapping("create")   // item/list.shop
	public ModelAndView addform() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("item",new Item());
		mav.setViewName("/item/add");
		return mav;
	}
	
//	@RequestMapping("create")   // item/list.shop
//	public String create(Model model) {
//  model.addAttribute(new item());
//		
//		return "item/add";
//	}
	
//	@RequestMapping("detail")
//	public ModelAndView detail(Integer id) {
//		ModelAndView mav = new ModelAndView();
//		Item item =service.getItem(id);
//		mav.addObject("item",item);
//		return mav;
//	}
	@RequestMapping("register")
	public ModelAndView add(@Valid Item item,BindingResult bresult ,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/add");
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		service.itemCreate(item,request);
		mav.setViewName("redirect:/item/list.shop");  //스프링에서 리다이렉트하는 방법
		return mav;
	}
	
	
	@RequestMapping("update")
	public ModelAndView update(@Valid Item item,BindingResult bresult ,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/edit");
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//db,파일 업로드
		service.itemUpdate(item,request);
		mav.setViewName("redirect:/item/detail.shop?id="+item.getId());
		return mav;
	}
	
	
//	@RequestMapping("delete")   // item/list.shop
//	public ModelAndView deleteform(Item item,BindingResult bresult ,HttpServletRequest request) {
//		ModelAndView mav = new ModelAndView();
//		service.itemdelete(item,request);
//		mav.setViewName("redirect:/item/list.shop");
//		return mav;
//		
//	}
	
	@GetMapping("delete")   // item/list.shop
	public ModelAndView deleteform(String id) {
		ModelAndView mav = new ModelAndView();
		service.itemdelete2(id);
		mav.setViewName("redirect:/item/list.shop");
		return mav;
		
	}
	
	//@GetMapping : get방식으로 들어왔을때만 
	@RequestMapping("*") // item/*.shop
	public ModelAndView detail(Integer id) {
		ModelAndView mav = new ModelAndView();
		Item item =service.getItem(id);
		mav.addObject("item",item);
		return mav;
	}
	

	

	
	
}
