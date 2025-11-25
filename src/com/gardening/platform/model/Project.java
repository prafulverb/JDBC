package com.gardening.platform.model;

public class Project {
    private int id;
    private int userId; // The gardener who owns the project
    private String name;
    private String description;
    private String progress; // e.g., "Started", "In Progress", "Completed"

    public Project(int id, int userId, String name, String description, String progress) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.progress = progress;
    }

    public Project(int userId, String name, String description, String progress) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.progress = progress;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getProgress() { return progress; }
    public void setProgress(String progress) { this.progress = progress; }
}
