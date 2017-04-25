package com.sergeev.studapp.model;

public class Group implements Identified {

    private Integer id;
    private String title;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Group setId(Integer id) {
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
