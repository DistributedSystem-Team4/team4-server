package Distributed.System.team4server.repository;

import Distributed.System.team4server.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);
    void deleteBoardByBoardId(Long boardId);
}
