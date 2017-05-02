package com.sergeev.studapp.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static com.sergeev.studapp.model.Constants.*;

@Entity
@Table(name = GROUPS,
        indexes = { @Index(columnList = TITLE) })
public class Group implements Identified {

    private Integer id;
    private String title;
    private Set<User> students = new HashSet<>();
    private Set<Course> courses = new HashSet<>();

    @Override
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @Override
    public Group setId(Integer id) {
        this.id = id;
        return this;
    }

    @Column(length = 30, nullable = false, unique = true)
    public String getTitle() {
        return title;
    }

    public Group setTitle(String title) {
        this.title = title;
        return this;
    }

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    public Set<User> getStudents() {
        return students;
    }

    public Group setStudents(Set<User> students) {
        this.students = students;
        return this;
    }

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    public Set<Course> getCourses() {
        return courses;
    }

    public Group setCourses(Set<Course> courses) {
        this.courses = courses;
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
