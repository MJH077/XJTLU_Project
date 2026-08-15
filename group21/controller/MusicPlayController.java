package com.cpt202.group21.controller;
import com.cpt202.group21.model.MusicFile;
import com.cpt202.group21.repository.MusicFileRepository;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/music") 
public class MusicPlayController {

    private final MusicFileRepository musicFileRepository;

    public MusicPlayController(MusicFileRepository musicFileRepository) {
        this.musicFileRepository = musicFileRepository;
    }

   
    @GetMapping("/play")
    public String showMusicPlayPage(@RequestParam(required = false) Long id, Model model) {
    
    List<MusicFile> allMusic = musicFileRepository.findByApproved(true);  // 你可以加排序或筛选

    model.addAttribute("musicList", allMusic);

    
    MusicFile currentMusic = null;
    if (id != null) {
        currentMusic = musicFileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Music not found"));
    } else if (!allMusic.isEmpty()) {
        currentMusic = allMusic.get(0);
    }

    model.addAttribute("music", currentMusic);
    return "MusicPlay";
    }

}