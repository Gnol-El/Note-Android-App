package com.example.noteappdemo.event;

import java.io.Serializable;

public class Event {
    int id;
    String title;
    String date;
    String time;
    String content;

    public Event(String title, String date, String time, String content) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.content = content;
    }

    public Event(int id, String title, String date, String time, String content) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
