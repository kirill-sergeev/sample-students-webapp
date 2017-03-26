package com.sergeev.studapp.model;


import java.sql.Date;
import java.sql.Time;

public class Lesson implements Identified {

    private String id;

    private Course course;
    private Date date;
    private Order order;
    private Type type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

        FIRST(1, Time.valueOf("07:45:00"), Time.valueOf("09:20:00")),
        SECOND(2, Time.valueOf("09:30:00"), Time.valueOf("11:05:00")),
        THIRD(3, Time.valueOf("11:15:00"), Time.valueOf("12:50:00")),
        FOURTH(4, Time.valueOf("13:10:00"), Time.valueOf("14:45:00")),
        FIFTH(5, Time.valueOf("14:55:00"), Time.valueOf("16:30:00")),
        SIXTH(6, Time.valueOf("16:45:00"), Time.valueOf("18:15:00"));

        private Integer number;
        private Time startTime;
        private Time endTime;

        Order(Integer number, Time startTime, Time endTime) {
            this.number = number;
            this.startTime = startTime;
            this.endTime = endTime;
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

        public Time getStartTime() {
            return startTime;
        }

        public Time getEndTime() {
            return endTime;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "number=" + number +
                    ", startTime=" + startTime +
                    ", endTime=" + endTime +
                    '}';
        }
    }
}
