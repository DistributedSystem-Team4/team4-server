package Distributed.System.team4server.controller;

import Distributed.System.team4server.controller.domain.dto.UserJoinRequest;
import Distributed.System.team4server.controller.domain.dto.UserLoginRequest;
import Distributed.System.team4server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<String> join(@RequestBody UserJoinRequest dto) {
		userService.join(dto.getUserName(), dto.getPassword(), dto.getUserId());
		return ResponseEntity.ok().body("회원가입이 성공했습니다");

	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserLoginRequest dto) {
		String token = userService.login(dto.getUserId(), dto.getPassword());
		return ResponseEntity.ok().body(token);
	}
}
