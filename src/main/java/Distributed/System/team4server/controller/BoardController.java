package Distributed.System.team4server.controller;

import Distributed.System.team4server.dto.board.BoardEditRequestDto;
import Distributed.System.team4server.dto.board.BoardPostRequestDto;
import Distributed.System.team4server.dto.DefaultResponseDto;
import Distributed.System.team4server.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @PreAuthorize("permitAll()")
    @GetMapping(value = "", params = "page")
    public ResponseEntity<DefaultResponseDto> getBoardListByPage(@PageableDefault(size = 10) Pageable pageable) {
        return boardService.getBoardList(pageable);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/info")
    public ResponseEntity<DefaultResponseDto> getBoardInfo(@RequestParam Long boardId) {
        return boardService.getBoardInfo(boardId);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/post")
    public ResponseEntity<DefaultResponseDto> createBoard(HttpServletRequest request, @RequestBody BoardPostRequestDto board) {
        return boardService.createBoard(request, board);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/edit")
    public ResponseEntity<DefaultResponseDto> editBoard(HttpServletRequest request, @RequestBody BoardEditRequestDto board) {
        return boardService.editBoard(request, board);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<DefaultResponseDto> deleteBoard(HttpServletRequest request, @RequestParam Long boardId) {
        return boardService.deleteBoard(request, boardId);
    }
}