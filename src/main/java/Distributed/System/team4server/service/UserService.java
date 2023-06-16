package Distributed.System.team4server.service;

import static Distributed.System.team4server.dto.DefaultResponseDto.result;

import Distributed.System.team4server.domain.Authority;
import Distributed.System.team4server.domain.User;
import Distributed.System.team4server.dto.DefaultResponseDto;
import Distributed.System.team4server.dto.user.UserDto;
import Distributed.System.team4server.dto.user.UserSaveRequestDto;
import Distributed.System.team4server.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HdfsService hdfsService;

    @Transactional(readOnly = true)
    public ResponseEntity<DefaultResponseDto> findUser(String userId) {
        LocalDateTime start = LocalDateTime.now(), end;
        String userLog = "";
        Optional<User> user = userRepository.findByUserId(userId);

        if (user.isPresent()) {
            end = LocalDateTime.now();
            userLog = "isExistUserId 401 /user/validate " + Duration.between(start, end).toMillis();
            hdfsService.cacheLog(userLog);
            return result(HttpStatus.CONFLICT, "이미 가입되어 있는 유저입니다.");
        }

        end = LocalDateTime.now();
        userLog = "isExistUserId 200 /user/validate " + Duration.between(start, end).toMillis();
        hdfsService.cacheLog(userLog);
        return result(HttpStatus.OK, "사용 가능한 아이디입니다.");
    }

    @Transactional
    public ResponseEntity<DefaultResponseDto> signUp(HttpServletRequest request, UserSaveRequestDto user) {
        LocalDateTime start = LocalDateTime.now(), end;
        String userLog = "";
        if (userRepository.findByUserId(user.getUserId()).isPresent()) {
            end = LocalDateTime.now();
            userLog = "register 401 /user/register " + Duration.between(start, end).toMillis();
            hdfsService.cacheLog(userLog);
            return result(HttpStatus.CONFLICT, "이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User newUser = User.builder()
                .userName(user.getUserName())
                .userId(user.getUserId())
                .passwd(passwordEncoder.encode(user.getPasswd()))
                .authorities(Collections.singleton(authority))
                .build();

        userRepository.save(newUser);
        end = LocalDateTime.now();
        userLog = "register 200 /user/register " + Duration.between(start, end).toMillis();
        hdfsService.cacheLog(userLog);

        return result(HttpStatus.OK, new UserDto(newUser));
    }
}
