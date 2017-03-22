package com.sergeev.studapp.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity("marks")
public class Mark implements Identified {
    @Id
    private String id;
    @Reference
    private Lesson lesson;
    @Reference
    private User student;
    private Integer value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
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
