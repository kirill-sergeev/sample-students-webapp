package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.DisciplineDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Discipline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class DisciplineService {

    private static final Logger LOG = LoggerFactory.getLogger(DisciplineService.class);
    private static final DisciplineDao DISCIPLINE_DAO = DaoFactory.getDaoFactory().getDisciplineDao();

    private static Discipline discipline;
    private static List<Discipline> disciplines;

    public static Discipline create(String title) throws ApplicationException {
        if (!checkTitle(title)){
            throw new ApplicationException("Bad parameters.");
        }

        discipline = new Discipline();
        discipline.setTitle(title);

        try {
            discipline = DISCIPLINE_DAO.persist(discipline);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return discipline;
    }

    public static Discipline read(String id) throws ApplicationException {

        try {
            discipline = DISCIPLINE_DAO.getById(id);
        } catch (PersistentException e) {
            throw new ApplicationException("Discipline not found.", e);
        }

        return discipline;
    }

    public static List<Discipline> readAll(){

        try {
            disciplines = DISCIPLINE_DAO.getAll();
        } catch (PersistentException e) {
            disciplines  = Collections.emptyList();
        }

        return disciplines;
    }

    public static Discipline update(String title, String id) throws ApplicationException {
        if (id == null || id.isEmpty() || !checkTitle(title)) {
            throw new ApplicationException("Bad parameters.");
        }

        discipline = new Discipline();
        discipline.setId(id);
        discipline.setTitle(title);

        try {
            DISCIPLINE_DAO.update(discipline);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot update discipline.", e);
        }

        return discipline;
    }

    public static void delete(String id) throws ApplicationException {
        if (id == null || id.isEmpty()){
            throw new ApplicationException("Bad parameters.");
        }

        try {
            DISCIPLINE_DAO.delete(id);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot delete group, because group not found.", e);
        }
    }

    private static boolean checkTitle(String title) {
        if (title == null || title.isEmpty()) {
            return false;
        }
        String expression = "(?u)^\\p{Lu}.{1,29}$";
        return title.matches(expression);
    }
}
