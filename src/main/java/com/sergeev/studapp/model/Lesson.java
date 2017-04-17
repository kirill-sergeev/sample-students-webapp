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

        PRACTICAL("1", "Practical"),
        LECTURE("2", "Lecture"),
        LAB("3", "Lab");

        private String id;
        private String type;

        Type(String id, String type) {
            this.id = id;
            this.type = type;
        }

        public static Type getById(String id) {
            for (Type type : values()) {
                if (type.id.equals(id)) return type;
            }
            return null;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return "Type{" +
                    "id=" + id +
                    ", type='" + type +
                    '}';
        }
    }

    public enum Order {

        FIRST(1, LocalTime.parse("07:45:00"), LocalTime.parse("09:20:00")),
        SECOND(2, LocalTime.parse("09:30:00"), LocalTime.parse("11:05:00")),
        THIRD(3, LocalTime.parse("11:15:00"), LocalTime.parse("12:50:00")),
        FOURTH(4, LocalTime.parse("13:10:00"), LocalTime.parse("14:45:00")),
        FIFTH(5, LocalTime.parse("14:55:00"), LocalTime.parse("16:30:00")),
        SIXTH(6, LocalTime.parse("16:45:00"), LocalTime.parse("18:15:00"));

        private Integer number;
        private LocalTime startLocalTime;
        private LocalTime endLocalTime;

        Order(Integer number, LocalTime startLocalTime, LocalTime endLocalTime) {
            this.number = number;
            this.startLocalTime = startLocalTime;
            this.endLocalTime = endLocalTime;
        }

        public static Order getByNumber(Integer number) {
            for (Order o : values()) {
                if (o.number.equals(number)) return o;
            }
            return null;
        }

        public Integer getNumber() {
            return number;
        }

        public LocalTime getStartTime() {
            return startLocalTime;
        }

        public LocalTime getEndTime() {
            return endLocalTime;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "number=" + number +
                    ", startLocalTime=" + startLocalTime +
                    ", endLocalTime=" + endLocalTime +
                    '}';
        }
    }
}
