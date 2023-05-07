package Distributed.System.team4server.dto.user;

import Distributed.System.team4server.domain.User;
import lombok.Getter;

@Getter
public class UserDto {
    private final String userId;
    private final String userName;

    public UserDto(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
    }
}
