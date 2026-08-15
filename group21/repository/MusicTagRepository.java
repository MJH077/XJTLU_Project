package com.cpt202.group21.repository;
import com.cpt202.group21.model.MusicTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MusicTagRepository extends JpaRepository<MusicTag, Long> {
}
