package com.book.user.login;

import com.book.model.Member;
import com.book.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = {"/user"})
public class LoginController {
    private final LoginService loginService;
    //private final SessionManager sessionManager;

    @GetMapping("/login")
    public String login(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                        LoginForm loginForm) {
        if (loginMember != null ) {
            return "/";
        }
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "user/login";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");

            return "user/login";
        }

        //createSession(loginMember, response);
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        session.setMaxInactiveInterval(1800);
        return "redirect:/";
    }

    //페이지 진입시 로그인 여부 확인
//	@GetMapping("/")
//	public String login(@SessionAttribute(name=SessionConst.LOGIN_MEMBER, required = false) Member member, Model model){

//		HttpSession session = request.getSession(false);
//		if(session == null){
//			return "home";
//		}
//		Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

//      @SessionAttribute(name=SessionConst.LOGIN_MEMBER, required = false)Member member
//      가 위 구문 대체가능함. 세션을 생성하지 않음

//		if(loginMember == null){
//			return "home";
//		}
//		model.addAttribute("member",loginMember);
//		return "loginHome";
//	}

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        //기존에 세션이 있으면 가져오고, 없으면 생성하지 않음.
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
