package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class GroupService {

    private static final Logger LOG = LoggerFactory.getLogger(GroupService.class);
    private static GroupDao groupDao = DaoFactory.getDaoFactory().getGroupDao();

    public static Group save(String title) {
        if (!checkTitle(title)) {
            throw new ApplicationException("Bad parameters.");
        }
        Group group = new Group().setTitle(title);
        groupDao.save(group);
        return group;
    }

    public static Group update(String title, int id) {
        Group group = new Group()
                .setId(id)
                .setTitle(title);
        groupDao.update(group);
        return group;
    }

    public static void remove(int id) {
        groupDao.remove(id);
    }


    public static Group get(int id) {
        return groupDao.getById(id);
    }

    public static List<Group> getAll() {
        return groupDao.getAll();
    }

    public static Map<Group, Integer> studentsCount() {
        Map<Group, Integer> groupsStudents = new LinkedHashMap<>();
        try (DaoFactory dao = DaoFactory.getDaoFactory()) {
            dao.startTransaction();
            List<Group> groups = dao.getGroupDao().getAll();
            for (Group group : groups) {
                int studentCount;
                try {
                    studentCount = dao.getUserDao().getByGroup(group.getId()).size();
                }catch (PersistentException e) {
                    studentCount = 0;
                }
                groupsStudents.put(group, studentCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groupsStudents;
    }

    private static boolean checkTitle(String title) {
        if (title == null || title.isEmpty()) {
            return false;
        }
        String expression = "(?u)^\\p{Lu}.{1,29}$";
        return title.matches(expression);
    }

    private GroupService() {
    }

}