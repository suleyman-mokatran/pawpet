package com.company.pawpet.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    @Lob
    @ElementCollection
    private List<byte[]> images;
    @Lob
    private String content;

    private LocalDateTime CreatedAt;
    private String type;
    private int petId;


    @ManyToOne
    @JoinColumn(name = "AppUserId")
    private AppUser appUser;

    public Post() {
    }

    public Post(List<byte[]> images, String content, LocalDateTime createdAt, String type, AppUser appUser) {
        this.images = images;
        this.content = content;
        CreatedAt = createdAt;
        this.type = type;
        this.appUser = appUser;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        CreatedAt = createdAt;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public List<byte[]> getImages() {
        return images;
    }

    public void setImages(List<byte[]> images) {
        this.images = images;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
