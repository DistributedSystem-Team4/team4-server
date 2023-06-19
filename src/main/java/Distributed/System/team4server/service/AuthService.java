package Distributed.System.team4server.service;

import static Distributed.System.team4server.dto.DefaultResponseDto.result;

import Distributed.System.team4server.config.jwt.TokenProvider;
import Distributed.System.team4server.domain.User;
import Distributed.System.team4server.dto.DefaultResponseDto;
import Distributed.System.team4server.dto.user.UserDto;
import Distributed.System.team4server.dto.user.UserLoginDto;
import Distributed.System.team4server.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final HdfsService hdfsService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public ResponseEntity<DefaultResponseDto> authorize(HttpServletRequest request, UserLoginDto userLoginDto) {
        LocalDateTime start = LocalDateTime.now(), end;
        String userLog = "";
        User user = userRepository.findByUserId(userLoginDto.getUserId()).orElse(null);

        if (user == null) {
            end = LocalDateTime.now();
            userLog = "POST 400 /user/login " + userLoginDto.getUserId() + " " + Duration.between(start, end).toMillis();
            hdfsService.cacheLog(userLog);
            log.info("{} LOGIN FAILED: 존재하지 않는 ID. (userId: {})", request, userLoginDto.getUserId());
            return result(HttpStatus.BAD_REQUEST, "존재하지 않는 ID입니다.");
        }

        if (!passwordEncoder.matches(userLoginDto.getPasswd(), user.getPasswd())) {
            end = LocalDateTime.now();
            userLog = "POST 400 /user/login " + userLoginDto.getUserId() + " " + Duration.between(start, end).toMillis();
            hdfsService.cacheLog(userLog);
            log.info("{} LOGIN FAILED: 비밀번호가 일치하지 않습니다. (userId: {})", request, userLoginDto.getUserId());
            return result(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        //log.info("로그인 시도중1");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginDto.getUserId(), userLoginDto.getPasswd());

        //log.info("로그인 시도중2, {}", authenticationToken);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //log.info("로그인 시도중3");
        String token = tokenProvider.createToken(authentication);

        end = LocalDateTime.now();
        userLog = "POST 200 /user/login " + userLoginDto.getUserId() + " " + Duration.between(start, end).toMillis();
        hdfsService.cacheLog(userLog);
        log.info("LOGIN SUCCESS: (userId: {})", userLoginDto.getUserId());
        return result(HttpStatus.OK, new UserDto(user), token);
    }
}
