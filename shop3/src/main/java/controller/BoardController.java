package controller;

import java.net.http.HttpRequest;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import Exception.BoardException;
import logic.Board;

import logic.ShopService;

@Controller
@RequestMapping("board")
public class BoardController {

	
	@Autowired
	private ShopService service;
	
	@GetMapping("*")
	public ModelAndView getBoard(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("board", new Board());
	
		return mav;
	}
	
	@PostMapping("write")
	public ModelAndView write(@Valid Board board,BindingResult bresult ,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
		return mav;
		}
		try {
			service.boardWrite(board,request);
			mav.setViewName("redirect:list.shop");
		}catch(Exception e) {
			e.printStackTrace();
			throw new BoardException("게시물 등록에 실패 했습니다." ,"write.shop");
		}
		return mav;
		
	
	
	}
	
	
}
