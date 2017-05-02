package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class GroupService {

    private static final Logger LOG = LoggerFactory.getLogger(GroupService.class);
    private static final GroupDao GROUP_DAO = DaoFactory.getDaoFactory().getGroupDao();

    public static Group save(String title) {
        if (!checkTitle(title)) {
            throw new ApplicationException("Bad parameters.");
        }
        Group group = new Group().setTitle(title);
        GROUP_DAO.save(group);
        return group;
    }

    public static Group update(String title, int id) {
        Group group = new Group()
                .setId(id)
                .setTitle(title);
        GROUP_DAO.update(group);
        return group;
    }

    public static void remove(int id) {
        GROUP_DAO.remove(id);
    }


    public static Group get(int id) {
        return GROUP_DAO.getById(id);
    }

    public static List<Group> getAll() {
        return GROUP_DAO.getAll();
    }
    
    public static Map<Group, Integer> studentsCount() {
        Map<Group, Integer> groupsStudents = new LinkedHashMap<>();
        List<Group> groups = GroupService.getAll();
        int studentCount;
        for (Group group : groups) {
            studentCount = UserService.getByGroup(group.getId()).size();
            groupsStudents.put(group, studentCount);
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