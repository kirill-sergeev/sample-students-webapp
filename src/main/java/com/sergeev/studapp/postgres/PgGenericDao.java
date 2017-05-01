package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.GenericDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Identified;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class PgGenericDao<T extends Identified> implements GenericDao<T> {

    protected abstract String getCreateQuery();
    protected abstract String getDeleteQuery();
    protected abstract String getSelectAllQuery();
    protected abstract String getSelectQuery();
    protected abstract String getUpdateQuery();
    protected abstract List<T> parseResultSet(ResultSet rs, Connection con);
    protected abstract void prepareStatementForInsert(PreparedStatement st, T object);
    protected abstract void prepareStatementForUpdate(PreparedStatement st, T object);

    @Override
    public void save(T object) {
        try (Connection con = PgDaoFactory.getConnection();
             PreparedStatement st = con.prepareStatement(getCreateQuery(),
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
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
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public T getById(Integer id) {
        return getByParams(getSelectQuery(), id).get(0);
    }

    @Override
    public List<T> getAll() {
        List<T> list;
        try (Connection con = PgDaoFactory.getConnection();
             PreparedStatement st = con.prepareStatement(getSelectAllQuery());
             ResultSet rs = st.executeQuery()) {
            list = parseResultSet(rs, con);
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        return list;
    }

    protected T getById(Integer id, Connection con) {
        T object;
        try (PreparedStatement st = con.prepareStatement(getSelectQuery())) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            object = parseResultSet(rs, con).get(0);
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        return object;
    }

    protected List<T> getByParams(String sql, Object... params) {
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
