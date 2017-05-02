package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.DisciplineDao;
import com.sergeev.studapp.model.Discipline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class DisciplineService {

    private static final Logger LOG = LoggerFactory.getLogger(DisciplineService.class);
    private static final DisciplineDao DISCIPLINE_DAO = DaoFactory.getDaoFactory().getDisciplineDao();

    public static Discipline save(String title) {
        if (!checkTitle(title)) {
            throw new ApplicationException("Bad parameters.");
        }
        Discipline discipline = new Discipline().setTitle(title);
        DISCIPLINE_DAO.save(discipline);
        return discipline;
    }

    public static Discipline update(String title, int id) {
        Discipline discipline = new Discipline()
                .setId(id)
                .setTitle(title);
        DISCIPLINE_DAO.update(discipline);
        return discipline;
    }

    public static void remove(int id) {
        DISCIPLINE_DAO.remove(id);
    }

    public static Discipline get(int id) {
        return DISCIPLINE_DAO.getById(id);
    }

    public static List<Discipline> getAll() {
        return DISCIPLINE_DAO.getAll();
    }
    
    private static boolean checkTitle(String title) {
        String expression = "(?u)^\\p{Lu}.{1,29}$";
        return !(title == null || title.isEmpty()) && title.matches(expression);
    }

    private DisciplineService() {
    }

}
