package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.GenericDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Identified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public abstract class PgGenericDao<T extends Identified> implements GenericDao<T>{

    private static final Logger LOG = LoggerFactory.getLogger(PgGenericDao.class);
    protected abstract String getSelectQuery();
    protected abstract String getSelectAllQuery();
    protected abstract String getCreateQuery();
    protected abstract String getUpdateQuery();
    protected abstract String getDeleteQuery();
    protected abstract List<T> parseResultSet(ResultSet rs);
    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object);
    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object);

    @Override
    public void save(T object) {
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(getCreateQuery(), PreparedStatement.RETURN_GENERATED_KEYS)) {
            prepareStatementForInsert(statement, object);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                object.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public void update(T object) {
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(getUpdateQuery())) {
            prepareStatementForUpdate(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistentException("On update modify more then 1 record: " + count);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public void remove(Integer id) {
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(getDeleteQuery())) {
            try {
                statement.setInt(1, id);
            } catch (Exception e) {
                throw new PersistentException(e);
            }
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistentException("On remove modify more then 1 record: " + count);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public T getById(Integer id) {
        List<T> list;
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(getSelectQuery())) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record with PK = " + id + " not found.");
        }
        if (list.size() > 1) {
            throw new PersistentException("Received more than one record.");
        }
        return list.iterator().next();
    }

    @Override
    public List<T> getAll() {
        List<T> list;
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(getSelectAllQuery());
             ResultSet rs = statement.executeQuery()) {
            list = parseResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
        return list;
    }

}
