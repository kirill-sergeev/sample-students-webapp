package com.sergeev.studapp.model;

public class Discipline implements Identified {

    private Integer id;
    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Discipline{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
