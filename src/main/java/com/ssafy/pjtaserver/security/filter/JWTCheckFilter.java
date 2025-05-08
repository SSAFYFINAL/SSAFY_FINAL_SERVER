package com.ssafy.pjtaserver.security.filter;

import com.google.gson.Gson;
import com.ssafy.pjtaserver.dto.UserDto;
import com.ssafy.pjtaserver.util.JWTUtil;
import com.ssafy.pjtaserver.util.apiResponseUtil.ApiResponse;
import com.ssafy.pjtaserver.util.apiResponseUtil.ApiResponseCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Slf4j
public class JWTCheckFilter extends OncePerRequestFilter {

    // 필터링
    // false : 체크한다 / true : 체크 안한다
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        if (request.getMethod().equals("OPTIONS") || path.startsWith("/api/public/")) {
            log.info("Path {} 제외됨 JWT filter.", path);
            return true; // 필터 제외
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("---------------------------");
        log.info("---------------------------");

        String authHeaderStr = request.getHeader("Authorization");

        try {
            String accessToken = authHeaderStr.substring(7);

            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            log.info("JWT claims: " + claims);
            String userLoginId = (String)claims.get("userLoginId");
            Boolean social = (Boolean) claims.get("social");
            String nickname = (String) claims.get("nickName");
            List<String> roleNames = (List<String>) claims.get("roleNames");
            String email = (String) claims.get("email");
            String userPwd = (String) claims.get("userPwd");
            String username = (String) claims.get("username");
            String userPhone = (String) claims.get("userPhone");

            UserDto userDto = new UserDto(userLoginId, userPwd, username, nickname, email, userPhone, social, roleNames);

            log.info("----------------------------------- ");
            log.info(String.valueOf(userDto));
            log.info(userDto.getAuthorities().toString());

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(userDto,userPwd,userDto.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("JWT Check Error ---------------");
            log.error(e.getMessage());

            Gson gson = new Gson();
            String msg = gson.toJson(ApiResponse.of(ApiResponseCode.ERROR_ACCESS_TOKEN));

            response.setContentType("application/json; charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();

        }
    }
}