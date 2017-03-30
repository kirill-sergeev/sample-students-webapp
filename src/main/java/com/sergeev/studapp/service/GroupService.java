package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GroupService {

    private static final Logger LOG = LoggerFactory.getLogger(GroupService.class);
    private static final GroupDao GROUP_DAO = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getGroupDao();

    private static Group group;
    private static List<Group> groups;

    public static Group create(String title) throws ApplicationException {
        if (!checkTitle(title)){
            throw new ApplicationException("Bad parameters.");
        }

        group = new Group();
        group.setTitle(title);

        try {
            group = GROUP_DAO.persist(group);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot save group.", e);
        }

        return group;
    }

    public static Group read(String id) throws ApplicationException {
        if (id == null || id.isEmpty()){
            throw new ApplicationException("Bad parameters.");
        }

        try {
            group = GROUP_DAO.getById(id);
        } catch (PersistentException e) {
            throw new ApplicationException("Group not found.", e);
        }

        return group;
    }

    public static List<Group> readAll() {

        try {
            groups = GROUP_DAO.getAll();
        } catch (PersistentException e) {
            groups  = Collections.emptyList();
        }

        return groups;
    }

    public static Group update(String title, String id) throws ApplicationException {
        if (id == null || id.isEmpty() || !checkTitle(title)) {
            throw new ApplicationException("Bad parameters.");
        }

        group = new Group();
        group.setId(id);
        group.setTitle(title);

        try {
            GROUP_DAO.update(group);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot update group.", e);
        }

        return group;
    }

    public static void delete(String id) throws ApplicationException {
        if (id == null || id.isEmpty()){
            throw new ApplicationException("Bad parameters.");
        }

        try {
            GROUP_DAO.delete(id);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot delete group, because group not found.", e);
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

    private static boolean checkTitle(String title) {
        if (title == null || title.isEmpty()) {
            return false;
        }
        String expression = "(?u)^\\p{Lu}\\w+$";
        return title.matches(expression);
    }
}