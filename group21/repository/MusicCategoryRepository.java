package com.cpt202.group21.repository;
import com.cpt202.group21.model.MusicCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MusicCategoryRepository extends JpaRepository<MusicCategory, Long> {
}
