package com.sergeev.studapp.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity("courses")
public class Course implements Identified {
    @Id
    private String id;
    @Reference
    private Discipline discipline;
    @Reference
    private Group group;
    @Reference
    private User teacher;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", discipline=" + discipline +
                ", group=" + group +
                ", teacher=" + teacher +
                '}';
    }


}
