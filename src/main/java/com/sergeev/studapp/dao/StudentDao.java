package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.Student;

import java.util.List;

public interface StudentDao extends GenericDao<Student, Integer> {
    List<Student> getByName(String name) throws PersistException;
}
