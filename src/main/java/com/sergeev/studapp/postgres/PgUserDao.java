package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.postgres.PgGroupDao.GROUP_ID;
import static java.sql.Types.NULL;

public class PgUserDao extends PgGenericDao<User> implements UserDao {

    protected static final String USER_ID = "user_id";
    protected static final String LOGIN = "login";
    protected static final String PASSWORD = "password";
    protected static final String FIRST_NAME = "first_name";
    protected static final String LAST_NAME = "last_name";
    protected static final String USER_TYPE = "type";

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM users WHERE user_id= ? ORDER BY first_name, last_name;";
    }

    @Override
    public String getSelectAllQuery() {
        return "SELECT * FROM users ORDER BY first_name, last_name;";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO users (" + LOGIN + ", " + PASSWORD + ", " + FIRST_NAME + ", " + LAST_NAME + ", " + USER_TYPE + ", " + GROUP_ID + ") VALUES (?, ?, ?, ?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE users SET "+ FIRST_NAME + "=?, " + LAST_NAME + "=?, " + GROUP_ID + "=? WHERE user_id= ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM users WHERE user_id= ?;";
    }

    @Override
    protected List<User> parseResultSet(ResultSet rs) throws PersistentException {
        List<User> result = new ArrayList<>();
        try {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getString(USER_ID));
                user.setFirstName(rs.getString(FIRST_NAME));
                user.setLastName(rs.getString(LAST_NAME));
                user.setLogin(rs.getString(LOGIN));
                user.setPassword(rs.getString(PASSWORD));
                user.setType(User.AccountType.getById(rs.getString(USER_TYPE)));
                if (user.getType() == User.AccountType.STUDENT) {
                    PgGroupDao pgd = new PgGroupDao();
                    user.setGroup(pgd.getById(rs.getString(GROUP_ID)));
                }
                result.add(user);
            }
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, User object) throws PersistentException {
        try {
            statement.setString(1, object.getLogin());
            statement.setString(2, object.getPassword());
            statement.setString(3, object.getFirstName());
            statement.setString(4, object.getLastName());
            statement.setInt(5, Integer.parseInt(object.getType().getId()));
            if (object.getType() == User.AccountType.STUDENT) {
                statement.setInt(6, Integer.parseInt(object.getGroup().getId()));
            } else {
                statement.setNull(6, NULL);
            }
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, User object) throws PersistentException {
        try {
            statement.setString(1, object.getFirstName());
            statement.setString(2, object.getLastName());
            if (object.getType() == User.AccountType.STUDENT) {
                statement.setInt(3, Integer.parseInt(object.getGroup().getId()));
            } else {
                statement.setNull(3, NULL);
            }
            statement.setInt(4, Integer.parseInt(object.getId()));
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public List<User> getByName(String name, User.AccountType type) throws PersistentException {
        List<User> list;
        String sql = "SELECT * FROM users WHERE lower(first_name||' '||last_name) LIKE (?) AND type= ?;";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + name + "%");
            statement.setInt(2, Integer.parseInt(type.getId()));
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public List<User> getByGroup(String groupId) throws PersistentException {
        List<User> list;
        String sql = "SELECT * FROM users WHERE group_id= ? AND type= 1;";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(groupId));
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return list;
    }

    @Override
    public List<User> getAll(User.AccountType type) throws PersistentException {
        List<User> list;
        String sql = "SELECT * FROM users WHERE type= ? ORDER BY first_name, last_name;";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(type.getId()));
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return list;
    }

    @Override
    public User getAccountInfo(String login) throws PersistentException {
        List<User> list;
        String sql = "SELECT * FROM users WHERE login= ?;";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record with login = " + login + " not found.");
        }
        if (list.size() > 1) {
            throw new PersistentException("Received more than one record.");
        }
        return list.iterator().next();
    }


}
