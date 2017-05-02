package com.sergeev.studapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.sergeev.studapp.model.Constants.*;

@Entity
@Table(name = USERS,
        indexes = {@Index(columnList = FIRST_NAME + "," + LAST_NAME)})
@SecondaryTable(name = ACCOUNTS,
        indexes = {@Index(columnList = LOGIN + "," + PASSWORD), @Index(columnList = TOKEN)})
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

    @AttributeOverrides({
            @AttributeOverride(name = LOGIN, column = @Column(table = ACCOUNTS, length = 61, nullable = false, unique = true)),
            @AttributeOverride(name = PASSWORD, column = @Column(table = ACCOUNTS, nullable = false)),
            @AttributeOverride(name = TOKEN, column = @Column(table = ACCOUNTS, unique = true))
    })
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

    @Column(name = FIRST_NAME, length = 30, nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    @Column(name = LAST_NAME, length = 30, nullable = false)
    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Column(name = ROLE, nullable = false)
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
        GUEST, STUDENT, TEACHER, ADMIN
    }

    @Embeddable
    public static class Account implements Serializable {

        private String login;
        private String password;
        private String token;

        public String getLogin() {
            return login;
        }

        public Account setLogin(String login) {
            this.login = login;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Account setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getToken() {
            return token;
        }

        public Account setToken(String token) {
            this.token = token;
            return this;
        }

        @Override
        public String toString() {
            return "Account{" +
                    "login='" + login + '\'' +
                    ", password='" + password + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }

    }

}
