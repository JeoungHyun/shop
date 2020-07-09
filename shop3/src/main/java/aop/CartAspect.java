package aop;

import org.springframework.core.annotation.Order;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import Exception.CartEmptyException;
import Exception.LoginException;
import logic.Cart;
import logic.User;

@Component
@Aspect
@Order(2)
public class CartAspect {

	@Around("execution(* controller.Cart*.check*(..))")
	public Object cartCheck(ProceedingJoinPoint joinPoint)	throws Throwable{
		HttpSession session = (HttpSession)joinPoint.getArgs()[0];
		User loginuser = (User)session.getAttribute("loginUser");
		Cart cart = (Cart)session.getAttribute("CART");
		
		if(loginuser==null) {
			throw new LoginException("주문은 회원만 가능합니다.","../user/login.shop");
		}
		if(cart == null || cart.getItemSetList().size()==0) {
			throw new CartEmptyException("주문할 상품이 장바구니에 없습니다.","../item/list.shop");
		}
		return joinPoint.proceed();
	}
}
