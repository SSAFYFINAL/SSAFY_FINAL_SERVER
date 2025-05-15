package com.ssafy.pjtaserver.security.filter;

import com.google.gson.Gson;
import com.ssafy.pjtaserver.dto.UserLoginDto;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.util.ApiResponse;
import com.ssafy.pjtaserver.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static com.ssafy.pjtaserver.enums.ApiResponseCode.*;

@Slf4j
public class JWTCheckFilter extends OncePerRequestFilter {

    // 필터링
    // false : 체크한다 / true : 체크 안한다
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (HttpMethod.OPTIONS.matches(method) || path.startsWith("/api/public/")) {
            log.info("-----------------------------------제외 프리패스-----------------------------------");
            log.info("Path {} 제외됨 JWT filter.", path);
            log.info("-----------------------------------제외 프리패스-----------------------------------");
            return true; // 필터링 제외
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("---------------------------");
        log.info("---------------------------");
        String authHeaderStr = request.getHeader("Authorization");

        if(authHeaderStr == null) {
            sendErrorResponse(response,ERROR_ACCESS_TOKEN);
            return;
        }

        if(!authHeaderStr.startsWith("Bearer ")) {
            sendErrorResponse(response, ERROR_TOKEN_FIELD);
            return;
        }

        String accessToken = authHeaderStr.substring(7);
        if(accessToken.isBlank()) {
            sendErrorResponse(response,ERROR_TOKEN_ISEMPTY);
            return;
        }

        try {
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            log.info("JWT claims: " + claims);
            String userLoginId = (String) claims.get("userLoginId");
            Boolean social = (Boolean) claims.get("social");
            String nickname = (String) claims.get("nickName");
            List<String> roleNames = (List<String>) claims.get("roleNames");
            String email = (String) claims.get("email");
            String userPwd = (String) claims.get("userPwd");
            String username = (String) claims.get("username");
            String userPhone = (String) claims.get("userPhone");

            UserLoginDto userLoginDto = new UserLoginDto(userLoginId, userPwd, username, nickname, email, userPhone, social, roleNames);

            log.info("----------------------------------- ");
            log.info(String.valueOf(userLoginDto));
            log.info(userLoginDto.getAuthorities().toString());

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(userLoginDto, userPwd, userLoginDto.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("JWT Check Error ---------------");
            log.error(e.getMessage());

            sendErrorResponse(response, ERROR_ACCESS_TOKEN);
        }
    }

    // 응답반환용 메서드
    private void sendErrorResponse(HttpServletResponse response, ApiResponseCode apiResponseCode) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json; charset=UTF-8");

        ApiResponse apiResponse = ApiResponse.of(apiResponseCode,apiResponseCode.getMessage()).getBody();
        String json = new Gson().toJson(apiResponse);

        PrintWriter out = response.getWriter();
        out.println(json);
        out.close();
    }
}