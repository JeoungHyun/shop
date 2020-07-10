package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import Exception.LoginException;
import logic.User;

@Component
@Aspect
@Order(3)
public class AdminAspect {

	
	@Around("execution(* controller.Admin*.*(..))") //*(..) 모든 메서드 !
	public Object adminCheck(ProceedingJoinPoint joinPoint) throws Throwable{
		User loginUser =null;
		for(Object o : joinPoint.getArgs()) {
			if(o instanceof HttpSession) {
				HttpSession session = (HttpSession)o;
				loginUser = (User)session.getAttribute("loginUser");
			}
		}
				
		if(loginUser== null) {
			throw new LoginException("로그인 후 거래하세요", "../user/login.shop");
		}
		if(!loginUser.getUserid().equals("admin")) {
			throw new LoginException("관리자만 사용가능합니다.", "../user/main.shop");
		}

		Object ret =  joinPoint.proceed();
		return ret;
	}
}
