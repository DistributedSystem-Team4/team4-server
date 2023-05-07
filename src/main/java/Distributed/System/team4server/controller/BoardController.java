package Distributed.System.team4server.controller;

import Distributed.System.team4server.dto.board.BoardEditRequestDto;
import Distributed.System.team4server.dto.board.BoardPostRequestDto;
import Distributed.System.team4server.dto.DefaultResponseDto;
import Distributed.System.team4server.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/post")
    public ResponseEntity<DefaultResponseDto> createBoard(HttpServletRequest request, @RequestBody BoardPostRequestDto board) {
        return boardService.createBoard(request, board);
    }

    @PutMapping("/edit")
    public ResponseEntity<DefaultResponseDto> editBoard(HttpServletRequest request, @RequestBody BoardEditRequestDto board) {
        if (!boardService.isExistedBoard(board.getBoardId())) {
            return DefaultResponseDto.result(HttpStatus.NOT_FOUND, null);
        }

        return boardService.editBoard(request, board);
    }

}
