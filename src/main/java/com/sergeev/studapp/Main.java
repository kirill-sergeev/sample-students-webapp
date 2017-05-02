package com.sergeev.studapp;

import com.sergeev.studapp.model.User;
import com.sergeev.studapp.postgres.PgUserDao;

public class Main {
    public static void main(String[] args) {
        PgUserDao dao = new PgUserDao();
        User user = new User();
        user.setRole(User.Role.TEACHER).setFirstName("Aaa").setLastName("Bbb");
        user.setAccount(new User.Account().setPassword("111").setLogin("aa-bbb"));
        dao.save(user);
        System.out.println(user);
    }
}