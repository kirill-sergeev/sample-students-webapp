package com.sergeev.studapp.model;

public class User implements Identified {

    private Integer id;
    private Account account;
    private Group group;
    private String firstName;
    private String lastName;
    private Role role;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public Account getAccount() {
        return account;
    }

    public User setAccount(Account account) {
        this.account = account;
        return this;
    }

    public Group getGroup() {
        return group;
    }

    public User setGroup(Group group) {
        this.group = group;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

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
