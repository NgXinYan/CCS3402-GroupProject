package GroupProject.Service;

import GroupProject.Entity.User;
import GroupProject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //REGISTER - save new user
    public boolean register(User user){
        //check if email already exists
        if(userRepository.findByEmail(user.getEmail())!=null){
            return false;
        }
        userRepository.save(user);
        return true;
    }

    //GET user by id
    public User getUserById(Long id){
        return userRepository.findById(id).get();
    }
}
