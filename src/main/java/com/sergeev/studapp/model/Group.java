package com.sergeev.studapp.model;

public class Group implements Identified {

    private String id;
    private String title;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Group setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Group setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
