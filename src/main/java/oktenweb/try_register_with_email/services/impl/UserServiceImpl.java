package oktenweb.try_register_with_email.services.impl;

import oktenweb.try_register_with_email.dao.UserDAO;
import oktenweb.try_register_with_email.models.ResponseTransfer;
import oktenweb.try_register_with_email.models.User;
import oktenweb.try_register_with_email.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean existsByEmail(String email) {
        return userDAO.existsByEmail(email);
    }

    @Override
    public ResponseTransfer save(User user) {

        if (userDAO.existsByUsername(user.getUsername())) {
            return new ResponseTransfer("User with such login already exists!!");
        } else if(userDAO.existsByEmail(user.getEmail())){
            return new ResponseTransfer("Field email is not unique!");
        }else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userDAO.save(user);
            return new ResponseTransfer("User has been saved successfully.");
        }
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public ResponseTransfer deleteById(int id){
        userDAO.delete(id);
        return new ResponseTransfer("User was deleted successfully");
    }

    // beacause  UserService extends UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findByUsername(username);
    }
}

