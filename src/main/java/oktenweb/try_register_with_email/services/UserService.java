package oktenweb.try_register_with_email.services;

import oktenweb.try_register_with_email.models.ResponseTransfer;
import oktenweb.try_register_with_email.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    ResponseTransfer save(User user);

    boolean existsByEmail(String email);

    List<User> findAll();

    ResponseTransfer deleteById(int id);
}
