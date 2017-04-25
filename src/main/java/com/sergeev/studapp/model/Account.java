package com.sergeev.studapp.model;


public class Account implements Identified {

    private Integer id;
    private String login;
    private String password;
    private String token;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Account setId(Integer id) {
        this.id = id;
        return this;
    }

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
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
