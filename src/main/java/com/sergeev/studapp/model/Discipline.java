package com.sergeev.studapp.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = Constants.DISCIPLINES)
public class Discipline implements Identified {

    private Integer id;
    private String title;
    private Set<Course> courses = new HashSet<>();

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @Override
    public Discipline setId(Integer id) {
        this.id = id;
        return this;
    }

    @Column(length = 30, nullable = false)
    public String getTitle() {
        return title;
    }

    public Discipline setTitle(String title) {
        this.title = title;
        return this;
    }

    @OneToMany(mappedBy = "discipline", cascade = CascadeType.ALL)
    public Set<Course> getCourses() {
        return courses;
    }

    public Discipline setCourses(Set<Course> courses) {
        this.courses = courses;
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
