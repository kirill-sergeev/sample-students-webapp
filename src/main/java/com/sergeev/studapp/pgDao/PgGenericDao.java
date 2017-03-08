package com.sergeev.studapp.pgDao;

import com.sergeev.studapp.dao.GenericDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Identified;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public abstract class PgGenericDao<T extends Identified<PK>, PK extends Integer> implements GenericDao<T, PK> {

    public abstract String getSelectQuery();
    public abstract String getSelectAllQuery();
    public abstract String getCreateQuery();
    public abstract String getUpdateQuery();
    public abstract String getDeleteQuery();

    protected abstract List<T> parseResultSet(ResultSet rs) throws PersistentException;
    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object) throws PersistentException;
    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws PersistentException;

    @Override
    public T persist(T object) throws PersistentException {
        String sql = getCreateQuery();
        try (Connection connection = PgDaoFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            prepareStatementForInsert(statement, object);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                    object.setId(resultSet.getInt(1));
            }
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return object;
    }

    @Override
    public T getByPK(Integer key) throws PersistentException {
        String sql = getSelectQuery();
        List<T> list;
        try (Connection connection = PgDaoFactory.createConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, key);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record with PK = " + key + " not found.");
        }
        if (list.size() > 1) {
            throw new PersistentException("Received more than one record.");
        }
        return list.iterator().next();
    }

    @Override
    public void update(T object) throws PersistentException {
        String sql = getUpdateQuery();
        try (Connection connection = PgDaoFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistentException("On update modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public void delete(T object) throws PersistentException {
        String sql = getDeleteQuery();
        try (Connection connection = PgDaoFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            try {
                statement.setObject(1, object.getId());
            } catch (Exception e) {
                throw new PersistentException(e);
            }
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistentException("On delete modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public void delete(PK key) throws PersistentException {
        String sql = getDeleteQuery();
        try (Connection connection = PgDaoFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            try {
                statement.setObject(1, key);
            } catch (Exception e) {
                throw new PersistentException(e);
            }
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistentException("On delete modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public List<T> getAll() throws PersistentException {
        List<T> list;
        String sql = getSelectAllQuery();
        try (Connection connection = PgDaoFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return list;
    }

    public PgGenericDao() {
    }
}
