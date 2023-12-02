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
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
            return "redirect:/";
        }
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form,
                        BindingResult bindingResult,
                        HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "user/login";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");

            return "user/login";
        }
        HttpSession session = request.getSession();
        /*
        Map<String, Object> sessionStore = new ConcurrentHashMap<>();
        String sesseionId = UUID.randomUUID().toString();
        sessionStore.put(sesseionId, loginMember);
        Cookie mySessionCookie = new Cookie(SessionConst.LOGIN_MEMBER, sesseionId);
         */

        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        session.setMaxInactiveInterval(1800);
        return "redirect:/";
    }

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
