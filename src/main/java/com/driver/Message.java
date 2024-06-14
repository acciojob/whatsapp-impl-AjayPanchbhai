package com.driver;

import java.util.Date;
import java.util.Objects;

public class Message {
    private int id;
    private String content;
    private Date timestamp;

    public Message() {
        this.timestamp = new Date();
    }

    public Message(int id, String content) {
        this.id = id;
        this.content = content;
        this.timestamp = new Date();
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Message message = (Message) object;
        return id == message.id && Objects.equals(content, message.content) &&
                Objects.equals(timestamp, message.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, timestamp);
    }
}
