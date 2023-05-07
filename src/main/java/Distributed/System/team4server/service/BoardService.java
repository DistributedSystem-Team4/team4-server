package Distributed.System.team4server.service;

import Distributed.System.team4server.domain.Board;
import Distributed.System.team4server.domain.User;
import Distributed.System.team4server.dto.board.BoardEditRequestDto;
import Distributed.System.team4server.dto.board.BoardPostRequestDto;
import Distributed.System.team4server.dto.DefaultResponseDto;
import Distributed.System.team4server.repository.BoardRepository;
import Distributed.System.team4server.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static Distributed.System.team4server.dto.DefaultResponseDto.result;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public ResponseEntity<DefaultResponseDto> createBoard(HttpServletRequest request, BoardPostRequestDto board) {
        User user = userRepository.findByUserId(board.getUserId()).get();

        Board build = Board.builder()
                .user(user)
                .title(board.getTitle())
                .content(board.getContent())
                .build();

        Board savedBoard = boardRepository.save(build);
        return result(HttpStatus.CREATED, savedBoard.getBoardId());
    }

    public ResponseEntity<DefaultResponseDto> editBoard(HttpServletRequest request, BoardEditRequestDto board) {
        Optional<Board> board1 = boardRepository.findById(board.getBoardId());

        board1.ifPresent(b -> {
            b.setTitle(board.getTitle());
            b.setContent(board.getContent());
            b.setEditedAt(LocalDateTime.now());
            boardRepository.save(b);
        });

        return result(HttpStatus.OK, null);
    }

    public boolean isExistedBoard(Long boardId) {
        Optional<Board> board = boardRepository.findById(boardId);
        return board.isPresent();
    }

}
