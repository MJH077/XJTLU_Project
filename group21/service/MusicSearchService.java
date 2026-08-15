package com.cpt202.group21.service;
import java.util.List;
import org.springframework.stereotype.Service;
import com.cpt202.group21.model.MusicFile;
import com.cpt202.group21.repository.MusicFileRepository;


@Service
public class MusicSearchService {
    private final MusicFileRepository musicFileRepository;


    public MusicSearchService(MusicFileRepository musicFileRepository) {
        this.musicFileRepository = musicFileRepository;
    }

    
    public List<MusicFile> searchMusic(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return musicFileRepository.findByApproved(true);
        }
        return musicFileRepository.searchByKeyword(keyword);
    }
}
