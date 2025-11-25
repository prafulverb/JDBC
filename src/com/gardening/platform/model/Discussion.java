package com.gardening.platform.model;

import java.sql.Timestamp;

public class Discussion {
    private int id;
    private int userId;
    private String topic;
    private String comment;
    private Timestamp timestamp;

    public Discussion(int id, int userId, String topic, String comment, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.topic = topic;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public Discussion(int userId, String topic, String comment) {
        this.userId = userId;
        this.topic = topic;
        this.comment = comment;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}
