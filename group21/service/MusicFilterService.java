package com.cpt202.group21.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.cpt202.group21.model.MusicFile;
import com.cpt202.group21.repository.MusicFileRepository;

@Service
public class MusicFilterService {
    private final MusicFileRepository musicFileRepository;

    public MusicFilterService(MusicFileRepository musicFileRepository) {
        this.musicFileRepository = musicFileRepository;
    }

    public List<MusicFile> filterMusic(Long categoryId, List<Long> tagIds) {
        boolean hasCategory = (categoryId != null && categoryId > 0);
        boolean hasTags = (tagIds != null && !tagIds.isEmpty());

        if (!hasCategory && !hasTags) {
            return musicFileRepository.findByApproved(true); // 返回所有已批准的音乐
        } else if (hasCategory && hasTags) {
            return musicFileRepository.filterByCategoryAndTags(categoryId, tagIds); // 分类和标签组合筛选
        } else if (hasCategory) {
            return musicFileRepository.findByCategoryId(categoryId); // 仅分类
        } else {
            return musicFileRepository.findByTagIds(tagIds); // 仅标签
        }
    }
}
