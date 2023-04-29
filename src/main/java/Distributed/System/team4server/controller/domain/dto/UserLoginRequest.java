package Distributed.System.team4server.controller.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginRequest {
    private String userName;
    private String userId;
    private String password;
}
