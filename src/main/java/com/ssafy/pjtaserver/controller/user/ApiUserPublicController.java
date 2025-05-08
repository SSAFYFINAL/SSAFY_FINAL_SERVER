package com.ssafy.pjtaserver.controller.user;

import com.ssafy.pjtaserver.dto.UserDto;
import com.ssafy.pjtaserver.service.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/public")
public class ApiUserPublicController {

    private final UserService userService;

    @PostMapping("/login")
    public void login(@RequestBody UserDto userDto,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws IOException, ServletException {
        userService.authenticateAndRespond(userDto, request, response);
    }

}
