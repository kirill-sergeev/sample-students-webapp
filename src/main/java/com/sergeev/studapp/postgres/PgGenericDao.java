package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.GenericDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Identified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class PgGenericDao<T extends Identified> implements GenericDao<T>{

    private static final Logger LOG = LoggerFactory.getLogger(PgGenericDao.class);
    protected abstract String getSelectQuery();
    protected abstract String getSelectAllQuery();
    protected abstract String getCreateQuery();
    protected abstract String getUpdateQuery();
    protected abstract String getDeleteQuery();
    protected abstract List<T> parseResultSet(ResultSet rs, Connection con);
    protected abstract void prepareStatementForInsert(PreparedStatement st, T object);
    protected abstract void prepareStatementForUpdate(PreparedStatement st, T object);

    @Override
    public void save(T object) {
        try (Connection con = PgDaoFactory.getConnection();
             PreparedStatement st = con.prepareStatement(getCreateQuery(), PreparedStatement.RETURN_GENERATED_KEYS)) {
            prepareStatementForInsert(st, object);
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                object.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public void update(T object) {
        try (Connection con = PgDaoFactory.getConnection();
             PreparedStatement st = con.prepareStatement(getUpdateQuery())) {
            prepareStatementForUpdate(st, object);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public void remove(Integer id) {
        try (Connection con = PgDaoFactory.getConnection();
             PreparedStatement st = con.prepareStatement(getDeleteQuery())) {
            try {
                st.setInt(1, id);
            } catch (Exception e) {
                throw new PersistentException(e);
            }
            int count = st.executeUpdate();
            if (count != 1) {
                throw new PersistentException("On remove modify more then 1 record: " + count);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public T getById(Integer id) {
        T  object;
        try (Connection con = PgDaoFactory.getConnection();
             PreparedStatement st = con.prepareStatement(getSelectQuery())) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            object = parseResultSet(rs, con).get(0);
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        if (object == null) {
            throw new PersistentException("Record with PK = " + id + " not found.");
        }
        return object;
    }

    @Override
    public List<T> getAll() {
        List<T> list;
        try (Connection con = PgDaoFactory.getConnection();
             PreparedStatement st = con.prepareStatement(getSelectAllQuery());
             ResultSet rs = st.executeQuery()) {
            list = parseResultSet(rs, con);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistentException(e);
        }
        return list;
    }

    protected T getById(Integer id, Connection con) {
        T  object;
        try (PreparedStatement st = con.prepareStatement(getSelectQuery())) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            object = parseResultSet(rs, con).get(0);
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        if (object == null) {
            throw new PersistentException("Record with PK = " + id + " not found.");
        }
        return object;
    }

    protected List<T> getBy(String sql, Object... params) {
        List<T> list;
        try (Connection con = PgDaoFactory.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                st.setObject(i + 1, params[i]);
            }
            ResultSet rs = st.executeQuery();
            list = parseResultSet(rs, con);
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        return list;
    }
}
