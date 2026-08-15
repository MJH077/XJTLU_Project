package com.cpt202.group21.model;

import java.time.LocalDate;
import java.util.*;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class MusicFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String artist;
    private String album;
    private String filePath;
    private LocalDate uploadDate = LocalDate.now();
    private boolean approved = false;
    private boolean blocked = false;

    // ✅ 明确指定分类中间表结构
    @ManyToMany
    @JoinTable(
        name = "music_file_category_id", // 你的实际中间表名
        joinColumns = @JoinColumn(name = "music_file_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id_id")
    )
    private Set<MusicCategory> categoryId = new HashSet<>();

    // ✅ 明确指定标签中间表结构
    @ManyToMany
    @JoinTable(
        name = "music_file_tag_id", // 你的实际中间表名
        joinColumns = @JoinColumn(name = "music_file_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id_id")
    )
    private Set<MusicTag> tagId = new HashSet<>();

    @ManyToOne
    @JsonBackReference
    private User uploader;

    public MusicFile() {}

    public MusicFile(Long id, String title, String artist, String album, String filePath, LocalDate uploadDate,
                     boolean approved, boolean blocked,
                     Set<MusicCategory> categoryId, Set<MusicTag> tagId, User uploader) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.filePath = filePath;
        this.uploadDate = uploadDate;
        this.approved = approved;
        this.blocked = blocked;
        this.categoryId = categoryId;
        this.tagId = tagId;
        this.uploader = uploader;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public String getFilePath() { return filePath; }
    public LocalDate getUploadDate() { return uploadDate; }
    public boolean isApproved() { return approved; }
    public boolean isBlocked() { return blocked; }
    public Set<MusicCategory> getCategoryId() { return categoryId; }
    public Set<MusicTag> getTagId() { return tagId; }
    public User getUploader() { return uploader; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setAlbum(String album) { this.album = album; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setUploadDate(LocalDate uploadDate) { this.uploadDate = uploadDate; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }
    public void setCategoryId(Set<MusicCategory> categoryId) { this.categoryId = categoryId; }
    public void setTagId(Set<MusicTag> tagId) { this.tagId = tagId; }
    public void setUploader(User uploader) { this.uploader = uploader; }
}
