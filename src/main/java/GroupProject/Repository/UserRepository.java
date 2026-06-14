package GroupProject.Repository;

import GroupProject.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //find user by email for login
    User findByEmail(String email);

    //check if email already registered
    boolean existsByEmail(String email);
}
