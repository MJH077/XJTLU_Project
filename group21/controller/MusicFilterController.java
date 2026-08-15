package com.cpt202.group21.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpt202.group21.model.MusicCategory;
import com.cpt202.group21.model.MusicFile;
import com.cpt202.group21.model.MusicTag;
import com.cpt202.group21.repository.MusicCategoryRepository;
import com.cpt202.group21.repository.MusicTagRepository;
import com.cpt202.group21.service.MusicFilterService;

@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@Controller
public class MusicFilterController {

    private final MusicFilterService musicFilterService;
    private final MusicCategoryRepository musicCategoryRepository;
    private final MusicTagRepository musicTagRepository;

    public MusicFilterController(
        MusicFilterService musicFilterService,
        MusicCategoryRepository musicCategoryRepository,
        MusicTagRepository musicTagRepository) {
        this.musicFilterService = musicFilterService;
        this.musicCategoryRepository = musicCategoryRepository;
        this.musicTagRepository = musicTagRepository;
    }

    // ✅ 修复后的筛选接口
    @GetMapping("/musicFilter")
    @ResponseBody
    public ResponseEntity<List<MusicFile>> filterMusic(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false, name = "tagIds") List<Long> tagIds) {
        System.out.println("🎯 categoryId = " + categoryId);
        System.out.println("🎯 tagIds = " + tagIds);
        List<MusicFile> result = musicFilterService.filterMusic(categoryId, tagIds);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/MusicFilter")
    public String showMusicFilterPage() {
        return "MusicFilter";
    }

    @GetMapping("/MusicCategoryCreate")
    public String showMusicCategoryCreatePage() {
        return "MusicCategoryCreate";
    }

    @GetMapping("/MusicCategoryDelete")
    public String showMusicCategoryDeletePage() {
        return "MusicCategoryDelete";
    }

    @GetMapping("/MusicFileUpdate")
    public String showMusicFileUpdatePage() {
        return "MusicFileUpdate";
    }

    @GetMapping("/MusicFileUpload")
    public String showMusicFileUploadPage() {
        return "MusicFileUpload";
    }

    @GetMapping("/api/music/categories")
    @ResponseBody
    public List<MusicCategory> getAllCategories() {
        return musicCategoryRepository.findAll();
    }

    @GetMapping("/api/music/tags")
    @ResponseBody
    public List<MusicTag> getAllTags() {
        return musicTagRepository.findAll();
    }
}
