package com.sergeev.studapp.model;


import javax.persistence.*;

@Entity
@Table(name = Constants.MARKS)
public class Mark implements Identified {

    private Integer id;
    private Lesson lesson;
    private User student;
    private Integer value;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @Override
    public Mark setId(Integer id) {
        this.id = id;
        return this;
    }

    @ManyToOne
    @JoinColumn
    public Lesson getLesson() {
        return lesson;
    }

    public Mark setLesson(Lesson lesson) {
        this.lesson = lesson;
        return this;
    }

    @ManyToOne
    @JoinColumn
    public User getStudent() {
        return student;
    }

    public Mark setStudent(User student) {
        this.student = student;
        return this;
    }

    @Column(name = Constants.VALUE, nullable = false)
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
