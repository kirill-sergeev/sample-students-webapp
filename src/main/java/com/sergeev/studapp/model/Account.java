package com.sergeev.studapp.model;

import javax.persistence.*;

@Entity
@Table(name = Constants.ACCOUNTS)
public class Account implements Identified {

    private Integer id;
    private String login;
    private String password;
    private String token;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @Override
    public Account setId(Integer id) {
        this.id = id;
        return this;
    }

    @Column(length = 61, nullable = false)
    public String getLogin() {
        return login;
    }

    public Account setLogin(String login) {
        this.login = login;
        return this;
    }

    @Column(nullable = false)
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
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
