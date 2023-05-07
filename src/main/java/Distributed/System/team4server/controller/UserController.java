package Distributed.System.team4server.controller;

import Distributed.System.team4server.dto.DefaultResponseDto;
import Distributed.System.team4server.dto.user.UserSaveRequestDto;
import Distributed.System.team4server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/validate")
    public ResponseEntity<DefaultResponseDto> checkExistUserId(HttpServletRequest request, @RequestParam String userId) {
        return userService.findUser(userId);
    }

    @PostMapping("/register")
    public ResponseEntity<DefaultResponseDto> register(HttpServletRequest request, @RequestBody UserSaveRequestDto body) {
        return userService.signUp(request, body);
    }
}
