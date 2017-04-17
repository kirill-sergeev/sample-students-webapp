package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.postgres.PgAccountDao.ACCOUNT_ID;
import static com.sergeev.studapp.postgres.PgGroupDao.GROUP_ID;
import static java.sql.Types.NULL;

public class PgUserDao extends PgGenericDao<User> implements UserDao {

    private static final Logger LOG = LoggerFactory.getLogger(PgUserDao.class);
    protected static final String USER_ID = "user_id";
    protected static final String FIRST_NAME = "first_name";
    protected static final String LAST_NAME = "last_name";
    protected static final String USER_ROLE = "role";

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM users WHERE user_id= ? ORDER BY first_name, last_name;";
    }
    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM users ORDER BY first_name, last_name;";
    }
    @Override
    protected String getCreateQuery() {
        return "INSERT INTO users (first_name, last_name, role, account_id, group_id) VALUES (?, ?, ?, ?, ?);";
    }
    @Override
    protected String getUpdateQuery() {
        return "UPDATE users SET first_name=?, last_name=?, group_id=? WHERE user_id=?;";
    }
    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM users WHERE user_id=?;";
    }

    @Override
    protected List<User> parseResultSet(ResultSet rs) throws PersistentException {
        List<User> result = new ArrayList<>();
        try {
            while (rs.next()) {
                User user = new User();
                PgAccountDao pad = new PgAccountDao();
                user.setId(rs.getString(USER_ID));
                user.setFirstName(rs.getString(FIRST_NAME));
                user.setLastName(rs.getString(LAST_NAME));
                user.setAccount(pad.getById(rs.getString(ACCOUNT_ID)));
                user.setRole(User.Role.valueOf(rs.getString(USER_ROLE)));
                if (user.getRole() == User.Role.STUDENT) {
                    PgGroupDao pgd = new PgGroupDao();
                    user.setGroup(pgd.getById(rs.getString(GROUP_ID)));
                }
                result.add(user);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, User object) throws PersistentException {
        try {
            statement.setString(1, object.getFirstName());
            statement.setString(2, object.getLastName());
            statement.setString(3, object.getRole().name());
            statement.setInt(4, Integer.parseInt(object.getAccount().getId()));
            if (object.getRole() == User.Role.STUDENT) {
                statement.setInt(5, Integer.parseInt(object.getGroup().getId()));
            } else {
                statement.setNull(5, NULL);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, User object) throws PersistentException {
        try {
            statement.setString(1, object.getFirstName());
            statement.setString(2, object.getLastName());
            if (object.getRole() == User.Role.STUDENT) {
                statement.setInt(3, Integer.parseInt(object.getGroup().getId()));
            } else {
                statement.setNull(3, NULL);
            }
            statement.setInt(4, Integer.parseInt(object.getId()));
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public List<User> getByName(String name, User.Role role) throws PersistentException {
        List<User> list;
        String sql = "SELECT * FROM users WHERE lower(first_name||' '||last_name) LIKE (?) AND role= ?;";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + name + "%");
            statement.setString(2, role.name());
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (SQLException e) {
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
        String sql = "SELECT * FROM users WHERE group_id= ? AND role='STUDENT';";;
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(groupId));
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public List<User> getAll(User.Role role) throws PersistentException {
        List<User> list;
        String sql = "SELECT * FROM users WHERE role= ? ORDER BY first_name, last_name;";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, role.name());
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    public User getByToken(String token) throws PersistentException {
        List<User> list;
        String sql = "SELECT * FROM users, accounts WHERE accounts.account_id=users.account_id AND token= ?";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, token);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        if (list.size() > 1) {
            throw new PersistentException("Received more than one record.");
        }
        return list.iterator().next();
    }

    @Override
    public User getByLogin(String login, String password) throws PersistentException {
        List<User> list;
        String sql = "SELECT * FROM accounts, users WHERE accounts.account_id = users.account_id AND accounts.login=? AND accounts.password=?";
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record not found.");
        }
        if (list.size() > 1) {
            throw new PersistentException("Received more than one record.");
        }
        return list.iterator().next();
    }

}
