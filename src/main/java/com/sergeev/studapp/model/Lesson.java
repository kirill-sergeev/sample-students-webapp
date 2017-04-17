package com.sergeev.studapp.model;


import java.time.LocalDate;
import java.time.LocalTime;

public class Lesson implements Identified {

    private String id;
    private Course course;
    private LocalDate date;
    private Order order;
    private Type type;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Lesson setId(String id) {
        this.id = id;
        return this;
    }

    public Course getCourse() {
        return course;
    }

    public Lesson setCourse(Course course) {
        this.course = course;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public Lesson setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public Order getOrder() {
        return order;
    }

    public Lesson setOrder(Order order) {
        this.order = order;
        return this;
    }

    public Type getType() {
        return type;
    }

    public Lesson setType(Type type) {
        this.type = type;
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
