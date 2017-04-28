package com.sergeev.studapp.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = Constants.COURSES)
public class Course implements Identified {

    private Integer id;
    private Discipline discipline;
    private Group group;
    private User teacher;
    private Set<Lesson> lessons = new HashSet<>();

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @Override
    public Course setId(Integer id) {
        this.id = id;
        return this;
    }

    @ManyToOne
    @JoinColumn
    public Discipline getDiscipline() {
        return discipline;
    }

    public Course setDiscipline(Discipline discipline) {
        this.discipline = discipline;
        return this;
    }

    @ManyToOne
    @JoinColumn
    public Group getGroup() {
        return group;
    }

    public Course setGroup(Group group) {
        this.group = group;
        return this;
    }

    @ManyToOne
    @JoinColumn
    public User getTeacher() {
        return teacher;
    }

    public Course setTeacher(User teacher) {
        this.teacher = teacher;
        return this;
    }

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    public Set<Lesson> getLessons() {
        return lessons;
    }

    public Course setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
        return this;
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
