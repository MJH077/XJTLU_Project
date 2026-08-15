package com.cpt202.group21.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cpt202.group21.model.Administrator;


@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    Optional<Administrator> findByName(String name); 
    Optional<Administrator> findByEmail(String email);
}