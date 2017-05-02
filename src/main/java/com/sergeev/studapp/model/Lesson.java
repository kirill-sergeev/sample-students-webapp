package com.sergeev.studapp.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static com.sergeev.studapp.model.Constants.*;
@Entity
@Table(name = LESSONS,
        uniqueConstraints = {@UniqueConstraint(columnNames = {COURSE_ID, DATE, ORDER, TYPE})})
public class Lesson implements Identified {

    private Integer id;
    private Course course;
    private LocalDate date;
    private Order order;
    private Type type;
    private Set<Mark> marks = new HashSet<>();

    @Override
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @Override
    public Lesson setId(Integer id) {
        this.id = id;
        return this;
    }

    @ManyToOne
    @JoinColumn(nullable = false)
    public Course getCourse() {
        return course;
    }

    public Lesson setCourse(Course course) {
        this.course = course;
        return this;
    }

    @Column(nullable = false)
    public LocalDate getDate() {
        return date;
    }

    public Lesson setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    @Column(name = ORDER, nullable = false)
    public Order getOrder() {
        return order;
    }

    public Lesson setOrder(Order order) {
        this.order = order;
        return this;
    }

    @Column(name = Constants.TYPE, nullable = false)
    public Type getType() {
        return type;
    }

    public Lesson setType(Type type) {
        this.type = type;
        return this;
    }

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    public Set<Mark> getMarks() {
        return marks;
    }

    public Lesson setMarks(Set<Mark> marks) {
        this.marks = marks;
        return this;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", course=" + course +
                ", date=" + date +
                ", order=" + order +
                ", type=" + type +
                '}';
    }

    public enum Type {
        PRACTICAL, LECTURE, LAB
    }

    public enum Order {

        FIRST(LocalTime.parse("07:45:00"), LocalTime.parse("09:20:00")),
        SECOND(LocalTime.parse("09:30:00"), LocalTime.parse("11:05:00")),
        THIRD(LocalTime.parse("11:15:00"), LocalTime.parse("12:50:00")),
        FOURTH(LocalTime.parse("13:10:00"), LocalTime.parse("14:45:00")),
        FIFTH(LocalTime.parse("14:55:00"), LocalTime.parse("16:30:00")),
        SIXTH(LocalTime.parse("16:45:00"), LocalTime.parse("18:15:00"));

        private LocalTime startTime;
        private LocalTime endTime;

        Order(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }
    }
}
