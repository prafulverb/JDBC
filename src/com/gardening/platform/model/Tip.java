package com.gardening.platform.model;

public class Tip {
    private int id;
    private int userId; // The gardener who shared the tip
    private String title;
    private String description;
    private String photoUrl; // Assuming URL or path for simplicity

    public Tip(int id, int userId, String title, String description, String photoUrl) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.photoUrl = photoUrl;
    }

    public Tip(int userId, String title, String description, String photoUrl) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.photoUrl = photoUrl;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}
