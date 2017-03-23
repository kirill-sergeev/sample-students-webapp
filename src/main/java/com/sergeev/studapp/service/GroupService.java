package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GroupService {

    private static GroupDao groupDao = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getGroupDao();

    public static Group create(String title) {
        Group group = new Group();
        group.setTitle(title);

        try {
            group = groupDao.persist(group);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return group;
    }

    public static Group read(String id) {
        Group group = null;

        try {
            group = groupDao.getById(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return group;
    }

    public static List<Group> readAll() {
        List<Group> groups = null;

        try {
            groups = groupDao.getAll();
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return groups;
    }

    public static Group update(String title, String id) {
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

    public static Map<Group, Integer> studentsCount() {
        Map<Group, Integer> groupsStudents = new LinkedHashMap<>();
        List<Group> groups = GroupService.readAll();
        int studentCount;

        for (Group group : groups) {
            studentCount = UserService.readByGroup(group.getId()).size();
            groupsStudents.put(group, studentCount);
        }

        return groupsStudents;
    }

}