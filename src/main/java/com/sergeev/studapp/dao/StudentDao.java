package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.Student;

import java.util.List;

public interface StudentDao extends GenericDao<Student> {

    List<Student> getByName(String name) throws PersistentException;

    List<Student> getByGroup(Integer groupId) throws PersistentException;
}
