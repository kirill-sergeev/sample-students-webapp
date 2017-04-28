package com.sergeev.studapp.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = Constants.USERS)
public class User implements Identified {

    private Integer id;
    private Account account;
    private Group group;
    private String firstName;
    private String lastName;
    private Role role;
    private Set<Course> courses = new HashSet<>();
    private Set<Mark> marks = new HashSet<>();

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @Override
    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    @OneToOne
    public Account getAccount() {
        return account;
    }

    public User setAccount(Account account) {
        this.account = account;
        return this;
    }

    @ManyToOne
    @JoinColumn
    public Group getGroup() {
        return group;
    }

    public User setGroup(Group group) {
        this.group = group;
        return this;
    }

    @Column(name = Constants.FIRST_NAME, length = 30, nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    @Column(name = Constants.LAST_NAME, length = 30, nullable = false)
    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    public Set<Course> getCourses() {
        return courses;
    }

    public User setCourses(Set<Course> courses) {
        this.courses = courses;
        return this;
    }

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    public Set<Mark> getMarks() {
        return marks;
    }

    public User setMarks(Set<Mark> marks) {
        this.marks = marks;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", account=" + account +
                ", group=" + group +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                '}';
    }

    public enum Role {
        STUDENT, TEACHER, ADMIN
    }
}
