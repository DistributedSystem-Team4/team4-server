package Distributed.System.team4server.dto.user;

import Distributed.System.team4server.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSaveRequestDto {

    private String userId;
    private String passwd;
    private String userName;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .passwd(passwd)
                .userName(userName)
                .build();
    }
}
