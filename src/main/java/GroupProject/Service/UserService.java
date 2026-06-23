package GroupProject.Service;

import GroupProject.Entity.User;
import GroupProject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return false;
        }
        userRepository.save(user);
        return true;
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // find user by email (used for setting tenant)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // update user profile
    public void updateProfile(User user) {
        userRepository.save(user);
    }

    // delete account
    public void deleteAccount(Long id) {
        userRepository.deleteById(id);
    }
}
