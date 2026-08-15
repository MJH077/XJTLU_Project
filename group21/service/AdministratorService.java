package com.cpt202.group21.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cpt202.group21.model.Administrator;
import com.cpt202.group21.repository.AdministratorRepository;

@Service
public class AdministratorService implements UserDetailsService {
    
    @Autowired
    private AdministratorRepository adminRepository;

    public Administrator saveAdmin(Administrator admin) {
        return adminRepository.save(admin);
    }

    public String loginAdmin(Administrator admin) {
        Optional<Administrator> ad = adminRepository.findByEmail(admin.getEmail());
        if (ad.isEmpty()) {
            return "Administrator Not Found!";
        } else if (!ad.get().getPassword().equals(admin.getPassword())) {
            return "Password Mismatch!";
        } else {
            return "Login success!";
        }
    }

    public Administrator getAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Admin not found with email: " + email));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Administrator admin = adminRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + email));
        
        return new User(
            admin.getEmail(),
            admin.getPassword(),
            AuthorityUtils.createAuthorityList("ROLE_ADMIN")
        );
    }
}