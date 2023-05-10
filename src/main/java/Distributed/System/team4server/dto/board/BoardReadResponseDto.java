package Distributed.System.team4server.dto.board;

import Distributed.System.team4server.domain.Board;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BoardReadResponseDto {

    private Long boardId;
    private String userId;
    private String userName;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;

    public BoardReadResponseDto(Board board) {
        this.boardId = board.getBoardId();
        this.userId = board.getUser().getUserId();
        this.userName = board.getUser().getUserName();
        this.title = board.getTitle();
        this.createdAt = board.getCreatedAt();
        this.editedAt = board.getEditedAt();
    }
}
