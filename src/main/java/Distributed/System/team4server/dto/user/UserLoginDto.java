package Distributed.System.team4server.dto.user;

import lombok.*;

/**
 * 유저가 로그인할 때 필요한 Dto (토큰 생성)
 */
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

    private String userId;
    private String passwd;
}
