package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupService {
    private static DaoFactory daoFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);
    private static GroupDao groupDao = daoFactory.getGroupDao();

    public static Group create(String title){
        Group group = new Group();
        group.setTitle(title);

        try {
            group = groupDao.persist(group);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return group;
    }

    public static List<Group> readAll(){
        List<Group> groups = new ArrayList<>();

        try {
            groups = groupDao.getAll();
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return groups;
    }

    public static Group update(String title, String id){
        Group group = new Group();
        group.setId(id);
        group.setTitle(title);

        try {
            groupDao.update(group);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return group;
    }

    public static void delete(String id) {
        try {
            groupDao.delete(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }
    }

}