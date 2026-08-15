package com.cpt202.group21.model;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class Music {
    private static final String UPLOAD_DIR = "uploads/";
    
    private List<MusicFile> musicFiles = new ArrayList<>();
    
    public static class MusicFile {
        private Long id;
        private String title;
        private String artist;
        private String album;
        private String category;
        private String filePath;
        private String coverPath;
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getArtist() { return artist; }
        public void setArtist(String artist) { this.artist = artist; }
        public String getAlbum() { return album; }
        public void setAlbum(String album) { this.album = album; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getFilePath() { return filePath; }
        public void setFilePath(String filePath) { this.filePath = filePath; }
        public String getCoverPath() { return coverPath; }
        public void setCoverPath(String coverPath) { this.coverPath = coverPath; }
    }
    
    @GetMapping("/music/filter")
    public String filterMusic(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String artist,
        Model model
    ) {
        List<MusicFile> filteredMusic = new ArrayList<>();
        
        for (MusicFile music : musicFiles) {
            if ((category == null || category.isEmpty() || music.getCategory().equalsIgnoreCase(category)) &&
                (artist == null || artist.isEmpty() || music.getArtist().equalsIgnoreCase(artist))) {
                filteredMusic.add(music);
            }
        }
        
        model.addAttribute("musicList", filteredMusic);
        return "music-filter-results";
    }
    
    @GetMapping("/music/search")
    public String searchMusic(@RequestParam String keyword, Model model) {
        List<MusicFile> searchResults = new ArrayList<>();
        
        for (MusicFile music : musicFiles) {
            if (music.getTitle().contains(keyword) ||
                music.getArtist().contains(keyword) ||
                music.getAlbum().contains(keyword)) {
                searchResults.add(music);
            }
        }
        
        model.addAttribute("searchResults", searchResults);
        return "music-search-results";
    }
    
    @GetMapping("/music/play/{id}")
    public String playMusic(@PathVariable Long id, Model model) {
        MusicFile music = findMusicById(id);
        if (music != null) {
            model.addAttribute("music", music);
            return "music-player"; 
        }
        return "error"; 
    }
    
    @GetMapping("/music/upload")
    public String showUploadForm() {
        return "music-upload-form"; 
    }
    
    @PostMapping("/music/upload")
    public String handleFileUpload(
        @RequestParam("title") String title,
        @RequestParam("artist") String artist,
        @RequestParam("album") String album,
        @RequestParam("category") String category,
        @RequestParam("musicFile") MultipartFile musicFile,
        @RequestParam("coverImage") MultipartFile coverImage,
        Model model
    ) {
        try {
            String musicFileName = saveFile(musicFile, "music");
            String coverFileName = saveFile(coverImage, "covers");
            
            MusicFile newMusic = new MusicFile();
            newMusic.setId(System.currentTimeMillis()); 
            newMusic.setTitle(title);
            newMusic.setArtist(artist);
            newMusic.setAlbum(album);
            newMusic.setCategory(category);
            newMusic.setFilePath(musicFileName);
            newMusic.setCoverPath(coverFileName);
            
            musicFiles.add(newMusic);
            
            model.addAttribute("success", true);
            return "redirect:/music/upload?success=true";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Upload failed");
            return "music-upload-form";
        }
    }
    
    private String saveFile(MultipartFile file, String subDir) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        
        Path uploadPath = Paths.get(UPLOAD_DIR + subDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        
        Files.write(filePath, file.getBytes());
        
        return fileName;
    }
    
    private MusicFile findMusicById(Long id) {
        for (MusicFile music : musicFiles) {
            if (music.getId().equals(id)) {
                return music;
            }
        }
        return null;
    }
}