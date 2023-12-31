package com.book.user.login;

import com.book.model.Member;
import com.book.session.SessionConst;
import com.book.user.login.member.MemberAddForm;
import com.book.user.login.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private final MemberRepository memberRepository;

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
                        HttpServletRequest request,
                        HttpServletResponse response) {

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
        if (form.isAutoLogin()) {
            //세션 아이디 저장
            loginMember.setSessionId(session.getId());
            memberRepository.save(loginMember);
            //쿠키저장
            Cookie loginCookie = new Cookie(SessionConst.LOGIN_MEMBER, session.getId());
            loginCookie.setMaxAge(60*60*24*7);
            loginCookie.setPath("/");
            response.addCookie(loginCookie);
        }
        return "redirect:/";
    }
    @GetMapping("/join")
    public String join(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember
        , @ModelAttribute("form") MemberAddForm form) {
        if (loginMember != null ) {
            return "redirect:/";
        }

        return "user/join";
    }

    @PostMapping("/join")
    public String joinAction(@Validated @ModelAttribute("form") MemberAddForm form, BindingResult bindingResult) {
        try {
            if (!bindingResult.hasErrors()) {
                Member member = memberRepository.findMemberByLoginId(form.getLoginId()).orElse(null);
                if ( member != null) {
                    bindingResult.reject("joinedMember");
                }
                if (!form.getPassword().equals(form.getPasswordCheck())) {
                    bindingResult.reject("passwordNotEquals");
                }
            }

            if (bindingResult.hasErrors()) {
                return "user/join";
            }

            Member member = Member.builder().loginId(form.getLoginId()).password(form.getPassword()).build();

            loginService.saveMember(member);
        } catch (Exception e) {
            log.error("joinAction error = {}", e.getMessage(), e);
            return "user/join";
        }

        return "redirect:login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        //기존에 세션이 있으면 가져오고, 없으면 생성하지 않음.
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            Cookie loginCookie = WebUtils.getCookie(request, SessionConst.LOGIN_MEMBER);
            if (loginCookie != null) {
                loginCookie.setPath("/");
                loginCookie.setMaxAge(0);
                response.addCookie(loginCookie);
                Member member = memberRepository.findMemberBySessionId(session.getId()).orElse(null);
                if (member != null) {
                    member.setSessionId(null);
                    memberRepository.save(member);
                }
            }
        }
        return "redirect:/";
    }
}
