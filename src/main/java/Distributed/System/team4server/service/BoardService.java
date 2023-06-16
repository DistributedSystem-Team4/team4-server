package Distributed.System.team4server.service;

import Distributed.System.team4server.domain.Board;
import Distributed.System.team4server.domain.User;
import Distributed.System.team4server.dto.board.BoardEditRequestDto;
import Distributed.System.team4server.dto.board.BoardPostRequestDto;
import Distributed.System.team4server.dto.DefaultResponseDto;
import Distributed.System.team4server.dto.board.BoardReadResponseDto;
import Distributed.System.team4server.repository.BoardRepository;
import Distributed.System.team4server.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final HdfsService hdfsService;

    public ResponseEntity<DefaultResponseDto> createBoard(HttpServletRequest request, BoardPostRequestDto board) {
        LocalDateTime start = LocalDateTime.now(), end;
        String boardLog = "";

        User user = userRepository.findByUserId(board.getUserId()).get();

        Board build = Board.builder()
                .user(user)
                .title(board.getTitle())
                .content(board.getContent())
                .build();

        Board savedBoard = boardRepository.save(build);

        end = LocalDateTime.now();
        boardLog = "createBoard 201 /board/post " + Duration.between(start, end).toMillis();
        hdfsService.cacheLog(boardLog);
        return result(HttpStatus.CREATED, savedBoard.getBoardId());
    }

    public ResponseEntity<DefaultResponseDto> editBoard(HttpServletRequest request, BoardEditRequestDto board) {
        LocalDateTime start = LocalDateTime.now(), end;
        String boardLog = "";

        if (!isExistBoard(board.getBoardId())) {
            end = LocalDateTime.now();
            boardLog = "editBoard 404 /board/edit " + Duration.between(start, end).toMillis();
            hdfsService.cacheLog(boardLog);
            return DefaultResponseDto.result(HttpStatus.NOT_FOUND, null);
        }

        Optional<Board> board1 = boardRepository.findById(board.getBoardId());

        log.info("before title:{}, before content:{}", board1.get().getTitle(), board1.get().getContent());
        log.info("after title:{}, after content:{}", board.getTitle(), board.getContent());

        board1.ifPresent(b -> {
            b.setTitle(board.getTitle());
            b.setContent(board.getContent());
            b.setEditedAt(LocalDateTime.now());
            boardRepository.save(b);
        });

        end = LocalDateTime.now();
        boardLog = "editBoard 200 /board/edit " + Duration.between(start, end).toMillis();
        hdfsService.cacheLog(boardLog);
        return result(HttpStatus.OK, null);
    }

    public ResponseEntity<DefaultResponseDto> deleteBoard(HttpServletRequest request, Long boardId) {
        LocalDateTime start = LocalDateTime.now(), end;
        String boardLog = "";

        if (!isExistBoard(boardId)) {
            end = LocalDateTime.now();
            boardLog = "deleteBoard 404 /board/delete " + Duration.between(start, end).toMillis();
            hdfsService.cacheLog(boardLog);
            return DefaultResponseDto.result(HttpStatus.NOT_FOUND, null);
        }

        boardRepository.deleteBoardByBoardId(boardId);
        end = LocalDateTime.now();
        boardLog = "deleteBoard 200 /board/delete " + Duration.between(start, end).toMillis();
        hdfsService.cacheLog(boardLog);
        return result(HttpStatus.OK, null);
    }

    public ResponseEntity<DefaultResponseDto> getBoardList(Pageable pageable) {
        LocalDateTime start = LocalDateTime.now(), end;
        String boardLog = "";

        if (!isValidPage((long) pageable.getPageNumber())) {
            end = LocalDateTime.now();
            boardLog = "getBoardList 404 /board " + Duration.between(start, end).toMillis();
            hdfsService.cacheLog(boardLog);
            return DefaultResponseDto.result(HttpStatus.NOT_FOUND, null);
        }

        Page<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc(pageable);
        long count = boardRepository.count();

        List<BoardReadResponseDto> list = new ArrayList<>();
        for (Board board : boardList) {
            BoardReadResponseDto readResponseDto = new BoardReadResponseDto(board);
            list.add(readResponseDto);
        }

        end = LocalDateTime.now();
        boardLog = "getBoardList 200 /board " + Duration.between(start, end).toMillis();
        hdfsService.cacheLog(boardLog);
        return result(HttpStatus.OK, list, count);
    }

    public ResponseEntity<DefaultResponseDto> getBoardInfo(Long boardId) {
        LocalDateTime start = LocalDateTime.now(), end;
        String boardLog = "";

        if (!isExistBoard(boardId)) {
            end = LocalDateTime.now();
            boardLog = "getBoardInfo 404 /board/info " + Duration.between(start, end).toMillis();
            hdfsService.cacheLog(boardLog);
            return DefaultResponseDto.result(HttpStatus.NOT_FOUND, null);
        }

        end = LocalDateTime.now();
        boardLog = "getBoardInfo 200 /board/info " + Duration.between(start, end).toMillis();
        hdfsService.cacheLog(boardLog);
        return result(HttpStatus.OK, boardRepository.findById(boardId).get());
    }

    private boolean isExistBoard(long boardId) {
        Optional<Board> board = boardRepository.findById(boardId);
        return board.isPresent();
    }

    private boolean isValidPage(Long pageNum) {
        long count = boardRepository.count();
        log.info("Total number of current posts : {}", count);
        return count / 10 >= pageNum;
    }
}