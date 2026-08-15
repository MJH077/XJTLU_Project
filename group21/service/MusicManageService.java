package com.cpt202.group21.service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.cpt202.group21.model.MusicFile;
import com.cpt202.group21.model.User;
import com.cpt202.group21.repository.MusicFileRepository;


@Service
public class MusicManageService {
    @Autowired
    private MusicFileRepository musicFileRepository;
    @Autowired
    private UserManageService userService;
    @Value("${file.upload-dir:/tem/uploads}")
    private String uploadDir;
    

    @Transactional
    public MusicFile uploadMusic(MusicFile musicFile, MultipartFile file, User user) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), uploadPath.resolve(filename));
        musicFile.setFilePath(filename);
        musicFile.setUploader(user);
        musicFile.setUploadDate(LocalDate.now());
        return musicFileRepository.save(musicFile);
    }
    

    public List<MusicFile> getAllApprovedMusic() { return musicFileRepository.findByApproved(true); }
    public List<MusicFile> getPendingApproval() { return musicFileRepository.findPendingApproval(); }
    public List<MusicFile> getBlockedContent() { return musicFileRepository.findByBlocked(true); }
    public List<MusicFile> getUserUploads(User user) { return musicFileRepository.findByUploader(user); }
    

    @Transactional
    public void approveMusic(Long id) {
        musicFileRepository.findById(id).ifPresent(music -> {
            music.setApproved(true);
            musicFileRepository.save(music);
        });
    }


    @Transactional
    public void blockMusic(Long id) {
        musicFileRepository.findById(id).ifPresent(music -> {
            music.setBlocked(true);
            music.setApproved(false);
            musicFileRepository.save(music);
            userService.incrementBlockedContentCount(music.getUploader());
        });
    }

    
    @Transactional
    public void deleteMusic(Long id) {
        musicFileRepository.deleteById(id);
    }
    public long countPendingApproval() {
        return musicFileRepository.countByApproved(false);
    }
}
    
