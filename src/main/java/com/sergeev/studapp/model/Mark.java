package com.sergeev.studapp.model;

public class Mark implements Identified {

    private String id;
    private Lesson lesson;
    private User student;
    private Integer value;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Mark setId(String id) {
        this.id = id;
        return this;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public Mark setLesson(Lesson lesson) {
        this.lesson = lesson;
        return this;
    }

    public User getStudent() {
        return student;
    }

    public Mark setStudent(User student) {
        this.student = student;
        return this;
    }

    public Integer getValue() {
        return value;
    }

    public Mark setValue(Integer value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "id=" + id +
                ", lesson=" + lesson +
                ", student=" + student +
                ", value=" + value +
                '}';
    }
}
