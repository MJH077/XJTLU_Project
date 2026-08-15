package com.cpt202.group21.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cpt202.group21.model.MusicCategory;
import com.cpt202.group21.model.MusicFile;
import com.cpt202.group21.service.UserMusicService;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/userMusic")
public class UserMusicController {
    @Autowired
    private UserMusicService userMusicService;


    // Create music category
    @PostMapping("/categories")
    public ResponseEntity<MusicCategory> createMusicCategory(@RequestBody MusicCategory musicCategory) {
        MusicCategory createdCategory = userMusicService.createMusicCategory(musicCategory);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }


    // Delete music category
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Void> deleteMusicCategory(@PathVariable Long categoryId) {
        userMusicService.deleteMusicCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    // Update music file
    @PutMapping("/files/{fileId}")
    public ResponseEntity<MusicFile> updateMusicFile(@PathVariable Long fileId, @RequestBody MusicFile updatedMusicFile) {
        MusicFile musicFile = userMusicService.updateMusicFile(fileId, updatedMusicFile);
        if (musicFile != null) {
            return new ResponseEntity<>(musicFile, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    
    // Upload music file
    @PostMapping("/files")
    public ResponseEntity<MusicFile> uploadMusicFile(@RequestBody MusicFile musicFile) {
        MusicFile uploadedFile = userMusicService.uploadMusicFile(musicFile);
        return new ResponseEntity<>(uploadedFile, HttpStatus.CREATED);
    }
    @GetMapping("/MusicPlay")
    public String showMusicPlayPage() {
        return "MusicPlay";
    }
}