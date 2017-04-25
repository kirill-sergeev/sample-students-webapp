package com.sergeev.studapp.model;




public class Course implements Identified {

    private Integer id;
    private Discipline discipline;
    private Group group;
    private User teacher;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Course setId(Integer id) {
        this.id = id;
        return this;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public Course setDiscipline(Discipline discipline) {
        this.discipline = discipline;
        return this;
    }

    public Group getGroup() {
        return group;
    }

    public Course setGroup(Group group) {
        this.group = group;
        return this;
    }

    public User getTeacher() {
        return teacher;
    }

    public Course setTeacher(User teacher) {
        this.teacher = teacher;
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
