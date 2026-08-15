package com.cpt202.group21.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class MusicTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public MusicTag() {}

    // Parameter Construction
    public MusicTag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    // ✅ Added equals and hashCode for JPA collection matching
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MusicTag)) return false;
        MusicTag tag = (MusicTag) o;
        return id != null && id.equals(tag.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}