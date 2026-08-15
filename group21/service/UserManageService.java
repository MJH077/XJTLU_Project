package com.cpt202.group21.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cpt202.group21.model.User;
import com.cpt202.group21.repository.UserRepository;


@Service
public class UserManageService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        if (user.isLocked()) {
            // 直接抛出Spring Security标准异常
            throw new LockedException("Account is locked");
        }
        
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
    }
    

    @Transactional
    public void lockUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setLocked(true);
            userRepository.save(user);
        });
    }


    @Transactional
    public void unlockUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setLocked(false);
            user.setBlockedContentCount(0);
            userRepository.save(user);
        });
    }


    @Transactional
    public void incrementBlockedContentCount(User user) {
        user.incrementBlockedContentCount();
        userRepository.save(user);
    }
    

    public List<User> getAllUsers() { return userRepository.findAll(); }
    public List<User> getActiveUsers() { return userRepository.findAllActiveUsers(); }
    public List<User> getLockedUsers() { return userRepository.findAllLockedUsers(); }
    public long countActiveUsers() { return userRepository.countActiveUsers(); }
    public long countLockedUsers() { return userRepository.countLockedUsers(); }
    
    
    static class LockedAccountException extends UsernameNotFoundException {
        public LockedAccountException(String msg) {
            super(msg);
        }
    }
}
