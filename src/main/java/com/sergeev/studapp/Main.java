package com.sergeev.studapp;

import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.User;
import com.sergeev.studapp.service.UserService;

public class Main {
    public static void main(String[] args) throws PersistentException {

//        Group group = GroupService.create("AA-2017");
//        User user = UserService.createStudent("Kirill", "Sergeev", group.getId());
//        user.getAccount().setToken("111");
//        AccountService.update(user);
//        System.out.println(user);

        long ji = System.currentTimeMillis();
        User user = UserService.readByToken("111");
        long jj = System.currentTimeMillis();
        System.out.println(user);
        System.out.println(jj - ji);


    }
}