package com.onrender.zipchatgo.member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // 일반 회원 로그인 시 사용하는 세션 키
    private static final String SESSION_KEY = "loginMemberId";

    // 게스트 로그인 여부를 저장하는 세션 키
    private static final String GUEST_SESSION_KEY = "guest";

    private final MemberService memberService;

    /**
     * 로그인
     * body: { "email": "...", "password": "..." }
     */
        @PostMapping("/login")
    public Map<String, Object> login(
            @RequestBody Map<String, String> body,
            HttpSession session,
            HttpServletResponse response) {

        Map<String, Object> result = new HashMap<>();

        try {
            Member member = memberService.login(
                    body.get("email"),
                    body.get("password")
            );

            // 기존 일반회원 로그인 세션
            session.setAttribute(SESSION_KEY, member.getId());

            // 혹시 이전에 게스트 세션이 있었다면 제거
            session.removeAttribute(GUEST_SESSION_KEY);

            // 로그인 상태 유지: 세션 30일 + 영속 쿠키 (체크 안 하면 기본 동작)
            if ("true".equals(body.get("remember"))) {
                Duration keep = Duration.ofDays(30);
                session.setMaxInactiveInterval((int) keep.getSeconds());
                ResponseCookie cookie = ResponseCookie.from("JSESSIONID", session.getId())
                        .path("/")
                        .httpOnly(true)
                        .sameSite("Lax")
                        .maxAge(keep)
                        .build();
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            }

            result.put("success", true);
            result.put("name", member.getName());
            result.put("email", member.getEmail());

        } catch (IllegalStateException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }

    /**
     * 회원가입 (일반회원 전용)
     * body: { "email": "...", "password": "...", "name": "...", "phone": "..." }
     */
    @PostMapping("/signup")
    public Map<String, Object> signup(
            @RequestBody Map<String, String> body,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        try {
            Member member = memberService.signup(
                    body.get("email"),
                    body.get("password"),
                    body.get("name"),
                    body.get("phone")
            );

            // 회원가입 성공 시 바로 로그인 상태로 세션 등록
            session.setAttribute(SESSION_KEY, member.getId());

            // 혹시 이전에 게스트 세션이 있었다면 제거
            session.removeAttribute(GUEST_SESSION_KEY);

            result.put("success", true);
            result.put("name", member.getName());
            result.put("email", member.getEmail());

        } catch (IllegalStateException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }

    /**
     * 게스트 로그인
     *
     * 게스트는 DB 회원이 아니기 때문에
     * 회원 ID 대신 세션에 guest=true를 저장한다.
     */
    @PostMapping("/guest")
    public Map<String, Object> guestLogin(HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        // 기존 일반회원 로그인 세션이 있다면 제거
        session.removeAttribute(SESSION_KEY);

        // 게스트 로그인 세션 생성
        session.setAttribute(GUEST_SESSION_KEY, true);

        result.put("success", true);
        result.put("name", "게스트");
        result.put("guest", true);

        return result;
    }

    /**
     * 비밀번호 재설정 (시연용 간이: 이메일 + 이름 확인)
     * body: { "email": "...", "name": "...", "newPassword": "..." }
     */
    @PostMapping("/reset-password")
    public Map<String, Object> resetPassword(@RequestBody Map<String, String> body) {

        Map<String, Object> result = new HashMap<>();

        try {
            memberService.resetPassword(
                    body.get("email"),
                    body.get("name"),
                    body.get("newPassword")
            );
            result.put("success", true);
        } catch (IllegalStateException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public Map<String, Object> logout(HttpSession session) {

        session.invalidate();

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);

        return result;
    }

    /**
     * 현재 로그인 상태 확인
     *
     * 일반회원:
     * loginMemberId가 있으면 로그인 상태
     *
     * 게스트:
     * guest=true이면 로그인 상태
     *
     * admin: 관리자 회원 또는 게스트이면 true (헤더의 관리자 메뉴 표시용)
     */
    @GetMapping("/check")
    public Map<String, Object> check(HttpSession session) {

        Object memberId = session.getAttribute(SESSION_KEY);
        Object guest = session.getAttribute(GUEST_SESSION_KEY);

        boolean isMemberLoggedIn = memberId != null;
        boolean isGuestLoggedIn = Boolean.TRUE.equals(guest);

        Map<String, Object> result = new HashMap<>();

        result.put("loggedIn", isMemberLoggedIn || isGuestLoggedIn);
        result.put("guest", isGuestLoggedIn);

        // 관리자 메뉴 표시 여부: 관리자 회원(ADMIN) 또는 게스트 체험 계정
        boolean isAdmin = isGuestLoggedIn;
        if (!isAdmin && memberId instanceof Long id) {
            try {
                isAdmin = "ADMIN".equals(memberService.getMember(id).getMemberType());
            } catch (IllegalStateException e) {
                isAdmin = false;
            }
        }
        result.put("admin", isAdmin);

        if (isGuestLoggedIn) {
            result.put("name", "게스트");
        }

        return result;
    }

    /**
     * 내 계정 정보 (계정 설정 페이지용)
     * 일반/소셜 회원만 성공, 게스트·비로그인은 success=false
     */
    @GetMapping("/me")
    public Map<String, Object> me(HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        Object idObj = session.getAttribute(SESSION_KEY);
        if (!(idObj instanceof Long memberId)) {
            result.put("success", false);
            result.put("guest", Boolean.TRUE.equals(session.getAttribute(GUEST_SESSION_KEY)));
            return result;
        }

        try {
            Member member = memberService.getMember(memberId);
            result.put("success", true);
            result.put("name", member.getName());
            result.put("email", member.getEmail());
            result.put("provider", member.getProvider());
            result.put("social", member.getPassword() == null);
        } catch (IllegalStateException e) {
            result.put("success", false);
            result.put("guest", false);
        }

        return result;
    }

    /**
     * 회원 탈퇴 (완전 삭제)
     * body: { "password": "...", "confirmText": "탈퇴" }
     */
    @PostMapping("/withdraw")
    public Map<String, Object> withdraw(
            @RequestBody Map<String, String> body,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        Object idObj = session.getAttribute(SESSION_KEY);
        if (!(idObj instanceof Long memberId)) {
            result.put("success", false);
            result.put("message", "회원 로그인 상태에서만 탈퇴할 수 있어요.");
            return result;
        }

        try {
            memberService.withdraw(memberId, body.get("password"), body.get("confirmText"));
            session.invalidate();
            result.put("success", true);
        } catch (IllegalStateException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }
}
