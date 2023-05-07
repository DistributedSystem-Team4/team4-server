package Distributed.System.team4server.dto.board;

import lombok.Data;

@Data
public class BoardEditRequestDto {

    private Long boardId;
    private String userId;
    private String title;
    private String content;
}
