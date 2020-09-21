package security.repository;

import model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author n.zhuchkevich
 * @since 09/21/2020
 * */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);
}
