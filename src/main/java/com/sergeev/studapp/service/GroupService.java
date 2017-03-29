package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GroupService {

    private static final Logger LOG = LoggerFactory.getLogger(GroupService.class);
    private static final GroupDao GROUP_DAO = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getGroupDao();

    public static Group create(String title) {
        Group group = new Group();
        group.setTitle(title);

        try {
            group = GROUP_DAO.persist(group);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return group;
    }

    public static Group read(String id) {
        Group group = null;

        try {
            group = GROUP_DAO.getById(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return group;
    }

    public static List<Group> readAll() {
        List<Group> groups = new ArrayList<>();

        try {
            groups = GROUP_DAO.getAll();
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
            GROUP_DAO.update(group);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return group;
    }

    public static void delete(String id) {
        try {
            GROUP_DAO.delete(id);
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