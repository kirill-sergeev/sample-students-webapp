package com.sergeev.studapp.model;

import java.sql.Date;
import java.sql.Time;

public class Lesson implements Identified {

    private Integer id;
    private Course course;
    private Date date;
    private Order order;
    private Type type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

        PRACTICAL(1, "Practical"),
        LECTURE(2, "Lecture"),
        LAB(3, "Lab");

        private Integer id;
        private String type;

        Type(Integer id, String type) {
            this.id = id;
            this.type = type;
        }

        public static Type getById(Integer id) {
            for (Type type : values()) {
                if (type.id.equals(id)) return type;
            }
            return null;
        }

        public Integer getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return "Type{" +
                    "id=" + id +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    public enum Order {

        FIRST(1, Time.valueOf("07:45:00"), Time.valueOf("09:20:00")),
        SECOND(2, Time.valueOf("09:30:00"), Time.valueOf("11:05:00")),
        THIRD(3, Time.valueOf("11:15:00"), Time.valueOf("12:50:00")),
        FOURTH(4, Time.valueOf("13:10:00"), Time.valueOf("14:45:00")),
        FIFTH(5, Time.valueOf("14:55:00"), Time.valueOf("16:30:00")),
        SIXTH(6, Time.valueOf("16:45:00"), Time.valueOf("18:15:00"));

        private Integer id;
        private Time startTime;
        private Time endTime;

        Order(Integer id, Time startTime, Time endTime) {
            this.id = id;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public static Order getById(Integer id) {
            for (Order order : values()) {
                if (order.id.equals(id)) return order;
            }
            return null;
        }

        public Integer getId() {
            return id;
        }

        public Time getStartTime() {
            return startTime;
        }

        public Time getEndTime() {
            return endTime;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "id=" + id +
                    ", startTime=" + startTime +
                    ", endTime=" + endTime +
                    '}';
        }
    }
}
