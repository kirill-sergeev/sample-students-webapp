package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.DisciplineDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Discipline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DisciplineService {

    private static final Logger LOG = LoggerFactory.getLogger(DisciplineService.class);
    private static final DisciplineDao DISCIPLINE_DAO = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getDisciplineDao();

    public static Discipline create(String title){
        Discipline discipline = new Discipline();
        discipline.setTitle(title);

        try {
            discipline = DISCIPLINE_DAO.persist(discipline);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return discipline;
    }

    public static Discipline read(String id) {
        Discipline discipline = null;

        try {
            discipline = DISCIPLINE_DAO.getById(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return discipline;
    }

    public static List<Discipline> readAll(){
        List<Discipline> disciplines = new ArrayList<>();

        try {
            disciplines = DISCIPLINE_DAO.getAll();
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return disciplines;
    }

    public static Discipline update(String title, String id){
        Discipline discipline = new Discipline();
        discipline.setId(id);
        discipline.setTitle(title);

        try {
            DISCIPLINE_DAO.update(discipline);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return discipline;
    }

    public static void delete(String id) {
        try {
            DISCIPLINE_DAO.delete(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }
    }

}
