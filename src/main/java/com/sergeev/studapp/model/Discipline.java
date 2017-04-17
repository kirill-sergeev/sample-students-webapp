package com.sergeev.studapp.model;


public class Discipline implements Identified {

    private String id;
    private String title;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Discipline setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Discipline setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public String toString() {
        return "Discipline{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
