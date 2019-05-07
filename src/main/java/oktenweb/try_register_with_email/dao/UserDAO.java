package oktenweb.try_register_with_email.dao;

import oktenweb.try_register_with_email.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
