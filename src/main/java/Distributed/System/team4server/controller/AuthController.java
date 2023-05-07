package Distributed.System.team4server.controller;

import Distributed.System.team4server.dto.DefaultResponseDto;
import Distributed.System.team4server.dto.user.UserLoginDto;
import Distributed.System.team4server.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<DefaultResponseDto> createJwt(HttpServletRequest request, @RequestBody UserLoginDto body) {
        return authService.authorize(request, body);
    }
}
