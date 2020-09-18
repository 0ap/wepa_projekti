package projekti;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserObjectRepository extends JpaRepository<UserObject, Long> {
    
    UserObject findByUsername(String username);
    UserObject findByShortlink(String shortlink);
    
}
