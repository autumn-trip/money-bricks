package com.moneybricks.common.security.filter;

import com.google.gson.Gson;
import com.moneybricks.common.util.JWTUtil;

import com.moneybricks.member.dto.MemberDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        log.info("check uri......................." + path);

        // /api/auth/ 경로는 JWT 필터를 적용하지 않음
        return path.startsWith("/api/auth") || path.startsWith("/api/member/signup")
                || path.startsWith("/api/member/check-duplicate") || path.startsWith("/api/dictionary");

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("------------------------JWTCheckFilter------------------");

        String authHeaderStr = request.getHeader("Authorization");

        try {
            if (authHeaderStr == null || !authHeaderStr.startsWith("Bearer ")) {
                throw new RuntimeException("Authorization Header is missing or invalid");
            }

            // "Bearer token"에서 토큰 부분만 추출
            String accessToken = authHeaderStr.substring(7);

            // JWT 토큰 검증 및 Claims 추출
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            log.info("JWT claims: " + claims);

            log.info("ID Class Type: {}", claims.get("id").getClass());

            // JWT Claims에서 데이터 추출
            Long id = Long.valueOf((Integer) claims.get("id"));
            String username = (String) claims.get("username");
            String password = (String) claims.get("password");
            String email = (String) claims.get("email");
            String name = (String) claims.get("name");
            String nickname = (String) claims.get("nickname");
            String phoneNumber = (String) claims.get("phoneNumber");
            String ssn = (String) claims.get("ssn");
            boolean emailAgreed = (boolean) claims.get("emailAgreed");
            boolean socialLinked = (boolean) claims.get("socialLinked");
            boolean deleted = (boolean) claims.get("deleted");
            boolean firstLoginFlag = (boolean) claims.get("firstLoginFlag");
            List<String> memberRoles = (List<String>) claims.get("memberRoles");

            // 탈퇴된 사용자일 경우 예외 발생
            if (deleted) {
                throw new RuntimeException("탈퇴한 사용자입니다.");
            }

            // MemberDTO 생성
            MemberDTO memberDTO = new MemberDTO(id, username, email,
                    password, name, nickname, phoneNumber,
                    ssn, emailAgreed, socialLinked, deleted, firstLoginFlag, memberRoles);

            log.info("-----------------------------------");
            log.info(memberDTO);
            log.info(memberDTO.getAuthorities());

            // Spring Security 인증 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(memberDTO, password, memberDTO.getAuthorities());

            // SecurityContextHolder에 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // 다음 필터로 전달
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("JWT Check Error..............");
            log.error(e.getMessage());

            // 에러 응답
            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();
        }
    }
}
