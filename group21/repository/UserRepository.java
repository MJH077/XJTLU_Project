package com.cpt202.group21.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.cpt202.group21.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email); 
    
    @Query("SELECT u FROM User u WHERE u.locked = false")
    List<User> findAllActiveUsers();
    @Query("SELECT u FROM User u WHERE u.locked = true")
    List<User> findAllLockedUsers();
    @Query("SELECT COUNT(u) FROM User u WHERE u.locked = false")
    long countActiveUsers();
    @Query("SELECT COUNT(u) FROM User u WHERE u.locked = true")
    long countLockedUsers();
}