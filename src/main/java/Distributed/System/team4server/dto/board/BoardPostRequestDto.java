package Distributed.System.team4server.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardPostRequestDto {

    private String userId;
    private String title;
    private String content;
}
