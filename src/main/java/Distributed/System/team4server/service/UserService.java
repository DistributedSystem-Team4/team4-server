package Distributed.System.team4server.service;

import Distributed.System.team4server.domain.User;
import Distributed.System.team4server.repository.UserRepository;
import Distributed.System.team4server.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;

	@Value("${jwt.token.secret}")
	private String key;
	private long expireTimeMs = 1000 * 60 * 60l;

	public String join(String userName, String password, String userId) {
		// userNmae 중복 check
		userRepository.findByUserId(userId)
			.ifPresent(user -> {
				throw new RuntimeException(userId+"는 이미 있습니다");
			});
		
		// 저장
		User user = User.builder()
				.userName(userName)
				.password(encoder.encode(password))
				.userId(userId)
				.build();
		userRepository.save(user);
		
		return "SUCCESS";
	}

	public String login(String userId, String password){
		// id 없음
		User selectedUser = userRepository.findByUserId(userId)
				.orElseThrow(()->new RuntimeException(userId+" 없습니다"));
		// password 틀림
//		logger.debug(selectedUser.getPassword(),password);
		if(!encoder.matches(password, selectedUser.getPassword())) {
			throw new RuntimeException("password 를 잘못 입력했습니다.");
		}
		// 토큰발행
		String token = JwtUtil.createJwt(selectedUser.getUserId(),key, expireTimeMs);
		return token;
	}

}
