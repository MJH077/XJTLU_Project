package com.cpt202.group21.service;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cpt202.group21.model.MusicCategory;
import com.cpt202.group21.model.MusicFile;
import com.cpt202.group21.repository.MusicCategoryRepository;
import com.cpt202.group21.repository.MusicFileRepository;


@Service
public class UserMusicService {
    @Autowired
    private MusicCategoryRepository musicCategoryRepository;
    @Autowired
    private MusicFileRepository musicFileRepository;


    // Create music category
    public MusicCategory createMusicCategory(MusicCategory musicCategory) {
        return musicCategoryRepository.save(musicCategory);
    }


    // Delete music category
    public void deleteMusicCategory(Long categoryId) {
        musicCategoryRepository.deleteById(categoryId);
    }


    // Update music file
    public MusicFile updateMusicFile(Long fileId, MusicFile updatedMusicFile) {
        Optional<MusicFile> existingMusicFile = musicFileRepository.findById(fileId);
        if (existingMusicFile.isPresent()) {
            MusicFile musicFile = existingMusicFile.get();
            musicFile.setTitle(updatedMusicFile.getTitle());
            musicFile.setArtist(updatedMusicFile.getArtist());
            musicFile.setAlbum(updatedMusicFile.getAlbum());
            musicFile.setFilePath(updatedMusicFile.getFilePath());
            musicFile.setUploadDate(updatedMusicFile.getUploadDate());
            musicFile.setApproved(updatedMusicFile.isApproved());
            musicFile.setBlocked(updatedMusicFile.isBlocked());
            musicFile.setCategoryId(updatedMusicFile.getCategoryId());
            musicFile.setTagId(updatedMusicFile.getTagId());
            musicFile.setUploader(updatedMusicFile.getUploader());
            return musicFileRepository.save(musicFile);
        }
        return null;
    }

    
    // Upload music file
    public MusicFile uploadMusicFile(MusicFile musicFile) {
        return musicFileRepository.save(musicFile);
    }
}