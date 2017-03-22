package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.DisciplineDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Discipline;

import java.util.ArrayList;
import java.util.List;

public class DisciplineService {
    private static DaoFactory daoFactory = DaoFactory.getDaoFactory(DaoFactory.POSTGRES);
    private static DisciplineDao disciplineDao = daoFactory.getDisciplineDao();

    public static Discipline create(String title){
        Discipline discipline = new Discipline();
        discipline.setTitle(title);

        try {
            discipline = disciplineDao.persist(discipline);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return discipline;
    }

    public static List<Discipline> readAll(){
        List<Discipline> disciplines = new ArrayList<>();

        try {
            disciplines = disciplineDao.getAll();
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
            disciplineDao.update(discipline);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return discipline;
    }

    public static void delete(String id) {
        try {
            disciplineDao.delete(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }
    }

    public static Discipline find(String id) {
        Discipline discipline = new Discipline();

        try {
            discipline = disciplineDao.getById(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return discipline;
    }

}
