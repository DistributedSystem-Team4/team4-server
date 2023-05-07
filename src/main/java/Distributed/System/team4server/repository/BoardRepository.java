package Distributed.System.team4server.repository;

import Distributed.System.team4server.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
