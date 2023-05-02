package Distributed.System.team4server.repository;

import Distributed.System.team4server.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserId(String userId);
}
