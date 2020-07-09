package aop;


import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import Exception.LoginException;
import logic.User;

@Component  // 객체화
@Aspect 	// AOP 실행 클래스
@Order(1)	// AOP 실행 순서
public class UserLoginAspect {
	//pointcut : controller 패키지의 User이름으로 시작하는 클래스.
	//메서드의 이름이 loginCheck 로 시작
	//매개변수는 상관없음     
	//args(..,session) : 마지막 매개변수가 session 인 메서드
	// 2개의 조건을 && 묶음
	
	@Around("execution(* controller.User*.loginCheck*(..)) && args(..,session)") //기본메서드 실행 전,후
	public Object userLoginCheck(ProceedingJoinPoint joinPoint , HttpSession session) throws Throwable{
		System.out.println("userLoginCheck");
		User loginUser =(User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException("[userlogin]로그인 후 거래하세요","login.shop");
		}
		return joinPoint.proceed();
	}
	
	
	/*
	 * 1.유효성검증
	 * 2.비밀번호 검증: 불일치
	 * 	 유효성 출력으로 error.login.password 코드 실행
	 * 3.비밀번호 일치
	 *  update 실행
	 *  로그인정보 수정. 단 admin이 다른사람의 정보 수정시는 로그인 정보 수정 안됨 
	 * 4.수정 완료 = ? mypage.shop으로 페이지 이동
	 * 
	 */
	@Around("execution(* controller.User*.check*(..)) && args(id,session)") //기본메서드 실행 전,후
	public Object check(ProceedingJoinPoint joinPoint ,String id, HttpSession session) throws Throwable{
		System.out.println("userLoginCheck");
		User loginUser =(User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException("[userlogin]로그인 후 거래하세요","login.shop");
		}
		
		if(!loginUser.getUserid().equals("admin") && !loginUser.getUserid().equals(id)) {
			throw new LoginException("본인정보만 조회가능","main.shop");
		}
		return joinPoint.proceed();
	}
	

}
