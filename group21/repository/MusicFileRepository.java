package com.cpt202.group21.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cpt202.group21.model.MusicFile;
import com.cpt202.group21.model.User;

@Repository
public interface MusicFileRepository extends JpaRepository<MusicFile, Long> {

    List<MusicFile> findByApproved(boolean approved);
    List<MusicFile> findByBlocked(boolean blocked);
    List<MusicFile> findByUploader(User user);
    List<MusicFile> findByUploaderAndBlocked(User user, boolean blocked);

    @Query("SELECT m FROM MusicFile m WHERE m.approved = false AND m.blocked = false")
    List<MusicFile> findPendingApproval();

    long countByApproved(boolean approved);

    // ✅ Keyword Search
    @Query("SELECT m FROM MusicFile m WHERE " +
           "LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.artist) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.album) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<MusicFile> searchByKeyword(@Param("keyword") String keyword);

   
    @Query("SELECT DISTINCT m FROM MusicFile m JOIN m.categoryId c " +
           "WHERE :categoryId IS NULL OR c.id = :categoryId")
    List<MusicFile> findByCategoryId(@Param("categoryId") Long categoryId);

    
    @Query("SELECT DISTINCT m FROM MusicFile m JOIN m.tagId t " +
           "WHERE :tagIds IS NOT NULL AND t.id IN :tagIds")
    List<MusicFile> findByTagIds(@Param("tagIds") List<Long> tagIds);

    @Query("SELECT DISTINCT m FROM MusicFile m JOIN m.categoryId c JOIN m.tagId t " +
       "WHERE c.id = :categoryId AND t.id IN :tagIds")
    List<MusicFile> filterByCategoryAndTags(@Param("categoryId") Long categoryId,@Param("tagIds") List<Long> tagIds);
    
}
