package security.repository;

import model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author n.zhuchkevich
 * @since 09/21/2020
 * */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);
}
