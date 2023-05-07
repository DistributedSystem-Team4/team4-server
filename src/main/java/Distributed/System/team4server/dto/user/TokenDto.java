package Distributed.System.team4server.dto.user;

import lombok.*;

/**
 * 토큰을 받을 때 사용
 */
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String token;
}
