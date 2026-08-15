package com.cpt202.group21.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.cpt202.group21.model.MusicFile;
import com.cpt202.group21.service.MusicSearchService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class MusicSearchController {

    private final MusicSearchService musicSearchService;

    public MusicSearchController(MusicSearchService musicSearchService) {
        this.musicSearchService = musicSearchService;
    }

   
    @GetMapping("/api/music/search")
    @ResponseBody
    public ResponseEntity<List<MusicFile>> searchMusic(@RequestParam(required = false) String keyword) {
        List<MusicFile> result = musicSearchService.searchMusic(keyword);
        return ResponseEntity.ok(result);
    }

    
    @GetMapping("/musicSearch")
    public String showMusicSearchPage() {
        return "MusicSearch";
    }
}
