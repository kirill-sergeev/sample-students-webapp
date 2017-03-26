package com.sergeev.studapp;

import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;
import com.sergeev.studapp.service.GroupService;
import com.sergeev.studapp.service.UserService;

public class Main {
    public static void main(String[] args) throws PersistentException {

        Group group = GroupService.create("AA-2017");
        User user = UserService.createStudent("Kirill", "Sergeev", group.getId());
        System.out.println(user);
    }
}