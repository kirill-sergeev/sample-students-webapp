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

import static com.sergeev.studapp.model.Constants.*;

public class PgUserDao extends PgGenericDao<User> implements UserDao {

    private static final Logger LOG = LoggerFactory.getLogger(PgUserDao.class);
    private static final String SQL_SELECT_USER_BY_LOGIN_AND_PASSWORD =
            "SELECT * FROM accounts, users WHERE accounts.account_id = users.account_id AND accounts.login = ? AND accounts.password = ?";
    private static final String SQL_SELECT_USER_BY_TOKEN =
            "SELECT * FROM users, accounts WHERE accounts.account_id = users.account_id AND token = ?";
    private static final String SQL_SELECT_USER_BY_ROLE =
            "SELECT * FROM users WHERE role = ? ORDER BY first_name, last_name";
    private static final String SQL_SELECT_USER_BY_GROUP =
            "SELECT * FROM users WHERE group_id = ? AND role = 'STUDENT'";
    private static final String SQL_SELECT_USER_BY_NAME_AND_ROLE =
            "SELECT * FROM users WHERE lower(first_name||' '||last_name) LIKE (?) AND role = ?";

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM users WHERE user_id = ? ORDER BY first_name, last_name";
    }

    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM users ORDER BY first_name, last_name";
    }

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO users (first_name, last_name, role, account_id, group_id) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE users SET first_name = ?, last_name = ?, group_id = ? WHERE user_id = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM users WHERE user_id = ?";
    }

    @Override
    protected List<User> parseResultSet(ResultSet rs, Connection con) {
        List<User> list = new ArrayList<>();
        try {
            while (rs.next()) {
                PgAccountDao accountDao = new PgAccountDao();
                User user = new User()
                        .setId(rs.getInt(USER_ID))
                        .setFirstName(rs.getString(FIRST_NAME))
                        .setLastName(rs.getString(LAST_NAME))
                        .setAccount(accountDao.getById(rs.getInt(ACCOUNT_ID), con))
                        .setRole(User.Role.valueOf(rs.getString(ROLE)));
                if (user.getRole() == User.Role.STUDENT) {
                    PgGroupDao groupDao = new PgGroupDao();
                    user.setGroup(groupDao.getById(rs.getInt(GROUP_ID), con));
                }
                list.add(user);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        if (list.isEmpty()) {
            throw new PersistentException("Record not found.");
        }
        return list;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement st, User user) {
        try {
            st.setString(1, user.getFirstName());
            st.setString(2, user.getLastName());
            st.setString(3, user.getRole().name());
            st.setInt(4, user.getAccount().getId());
            if (user.getRole() == User.Role.STUDENT) {
                st.setInt(5, user.getGroup().getId());
            } else {
                st.setNull(5, java.sql.Types.NULL);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement st, User user) {
        try {
            st.setString(1, user.getFirstName());
            st.setString(2, user.getLastName());
            if (user.getRole() == User.Role.STUDENT) {
                st.setInt(3, user.getGroup().getId());
            } else {
                st.setNull(3, java.sql.Types.NULL);
            }
            st.setInt(4, user.getId());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public List<User> getByName(String name, User.Role role) {
        return getBy(SQL_SELECT_USER_BY_NAME_AND_ROLE, "%" + name.toLowerCase() + "%", role.name());
    }

    @Override
    public List<User> getByGroup(Integer groupId) {
        return getBy(SQL_SELECT_USER_BY_GROUP, groupId);
    }

    @Override
    public List<User> getAll(User.Role role) {
        return getBy(SQL_SELECT_USER_BY_ROLE, role.name());
    }

    @Override
    public User getByToken(String token) {
        return getBy(SQL_SELECT_USER_BY_TOKEN, token).get(0);
    }

    @Override
    public User getByLogin(String login, String password) {
        return getBy(SQL_SELECT_USER_BY_LOGIN_AND_PASSWORD, login, password).get(0);
    }

}