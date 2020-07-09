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
@RequestMapping("user")  
public class UserController {
	
	@Autowired
	private ShopService service;
	
	@GetMapping("*")   
	public String userEntry(Model model) {
		model.addAttribute(new User());
		return null;
	}
	

	@PostMapping("userEntry")
	public ModelAndView user(@Valid User user,BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			
			bresult.reject("error.input.user");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		
		try {
			service.userInsert(user);
			mav.setViewName("redirect:login.shop");
			
		}catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			bresult.reject("error.duplicate.user");
			mav.getModel().putAll(bresult.getModel());
		}
		
		return mav;
	}
	@PostMapping("login")
	public ModelAndView login(@Valid User user,BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			return mav;
		}
		
		//1.db의 정보의 id와password 비교
		//2.일치 : session loginUser 정보 저장
		//3.불일치: 비밀번호 확인 내용 출력
		//4.db에 해당 id정보가 없는 경우 내용 출력
		try {
		User dbUser = service.getUser(user.getUserid());
		if(user.getPassword().equals(dbUser.getPassword())) {
			session.setAttribute("loginUser", dbUser);
			mav.setViewName("redirect:main.shop");
		}else {  //비밀번호가 틀릴 경우
			bresult.reject("error.login.password");
		}
		}catch(EmptyResultDataAccessException e) {
			e.printStackTrace();
			bresult.reject("error.login.id");
		}

		return mav;
	}
	
	@RequestMapping("logout")
	public String loginChecklogout(HttpSession session) {
		session.invalidate();
		return "redirect:login.shop";
	}
	
	@RequestMapping("main")
	//login 되어야 실행 가능. 메서드 이름 loginxxx로 지정
	public String loginCheckmain(HttpSession session) {
		return null;
	}
	
/*
 * AOP 설정하기
 * 1.UserController 의 check로 시작하는 메서드에 매개변수가 id,session인 경우.
 * -로그인 안된경우 : 로그인하세요. => login.shop 페이지 이동
 * -admin이 아니면서 , 다른 아이디 정보 조회시 . 본인정보만 조회가능 -> main.shop페이지 이동
 * */
	
	@RequestMapping("mypage")
	public ModelAndView checkmypage(String id, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(id);
		//sale 테이블에서 saleid,userid,saledate 컬럼값만 저장된 Sale 객체의 List 형태로 리턴
		List<Sale> salelist = service.salelist(id);
		for(Sale sa : salelist) {
			List<SaleItem> saleitemlist= service.saleItemList(sa.getSaleid());
			int sum = 0;
			for(SaleItem si : saleitemlist) {
				Item item = service.getItem(Integer.parseInt(si.getItemid()));
				si.setItem(item);
				sum += (si.getQuantity() * si.getItem().getPrice()); 
			}
			sa.setItemList(saleitemlist);
			sa.setTotal(sum);
		}
		mav.addObject("user",user);
		mav.addObject("salelist",salelist);
		return mav;
	}
	
	
	@GetMapping(value= {"update","delete"})
	public ModelAndView checkview(String id , HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(id);
		mav.addObject("user",user);
		return mav;
	}


	@PostMapping("update")
	public ModelAndView update(@Valid User user, BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		//유효성 검증
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			return mav;
		}
		//비밀번호 검증
		User loginUser = (User)session.getAttribute("loginUser");
		//로그인한 정보의 비밀번호와 입력된 비밀번호 검증
		if(!user.getPassword().equals(loginUser.getPassword())) {
			bresult.reject("error.login.password");
			return mav;
		}
		//비밀번호 일치 :수정가능
		try {
			service.userUpdate(user);
			mav.setViewName("redirect:mypage.shop?id="+user.getUserid());
			//로그인 정보 수정
			if(loginUser.getUserid().equals(user.getUserid())) {
				session.setAttribute("loginUser", user);
			}
		}catch(Exception e) {
			e.printStackTrace();
			bresult.reject("error.user.update");
		}
		return mav;
	}
	/*
	 * 회원 탈퇴
	 * 1.비밀번호 검증 불일치 : "비밀번호 오류 메세지 출력. delete.shop 이동
	 * 2.비밀번호 검증 일치
	 *  		회원 db에서 delete 하기
	 *  		본인인경우 : logout 하고 , login.shop 페이지 요청
	 *  		관리자인경우 : main.shop으로 가기
	 */
	@PostMapping("delete")
	public ModelAndView checkdelete(String userid, String password, HttpSession session) {
		ModelAndView mav= new ModelAndView();
		User loginUser = (User)session.getAttribute("loginUser");
		if(userid.equals("admin")) {
			throw new LoginException("관리자는 탈퇴 불가능합니다.","main.shop");
			
		}
		if(!loginUser.getPassword().equals(password)) {
			throw new LoginException("회원탈퇴시 비빌번호가 틀립니다.","delete.shop?id="+userid);
		}
			try {
				service.userDelete(userid);
			}catch(Exception e) {
				e.printStackTrace();
				throw new LoginException("회원 탈퇴시 오류가 발생했습니다. 전산부에 연락 요망","delete.shop?id="+userid);
			}
			if(loginUser.getUserid().equals("admin")) {
				mav.setViewName("redircet:/user/main.shop");
			}else {
				session.invalidate();
				throw new LoginException(userid+"회원님 탈퇴되었습니다","login.shop");
			}
			
	return mav;
}
}