package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.dao.UserDao;
import com.sergeev.studapp.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.model.Constants.*;

public class PgUserDao extends PgGenericDao<User> implements UserDao {

    private static final String SQL_SELECT_USER_BY_LOGIN_AND_PASSWORD =
            "SELECT * FROM accounts a, users u WHERE a.id = u.id AND a.login = ? AND a.password = ?";
    private static final String SQL_SELECT_USER_BY_TOKEN =
            "SELECT * FROM accounts a, users u WHERE a.id = u.id AND token = ?";
    private static final String SQL_SELECT_USER_BY_ROLE =
            "SELECT * FROM accounts a, users u WHERE a.id = u.id AND role_id = ? ORDER BY first_name, last_name";
    private static final String SQL_SELECT_USER_BY_GROUP =
            "SELECT * FROM accounts a, users u WHERE a.id = u.id AND group_id = ?";
    private static final String SQL_SELECT_USER_BY_NAME_AND_ROLE =
            "SELECT * FROM accounts a, users u WHERE a.id = u.id AND lower(first_name||' '||last_name) LIKE (?) AND role_id = ?";

    public PgUserDao(Connection con) {
        super(con);
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM accounts a, users u WHERE a.id = u.id AND u.id = ? ORDER BY first_name, last_name";
    }

    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM accounts a, users u WHERE a.id = u.id ORDER BY first_name, last_name";
    }

    @Override
    protected String getCreateQuery() {
        return "WITH ins AS (" +
                "INSERT INTO users (first_name, last_name, role_id, group_id) " +
                "VALUES (?, ?, ?, ?) RETURNING id) " +
                "INSERT INTO accounts (id, login, password) " +
                "VALUES ((SELECT id FROM ins), ?, ?) RETURNING id";
    }

    @Override
    protected String getUpdateQuery() {
        return "WITH upd AS (UPDATE users u SET first_name = ?, last_name = ?, group_id = ? WHERE u.id = ? " +
                "RETURNING *) " +
                "UPDATE accounts a SET login = ?, password = ?, token = ? WHERE a.id = (SELECT id FROM upd)";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM users WHERE id = ?";
    }

    @Override
    protected List<User> parseResultSet(ResultSet rs) {
        List<User> list = new ArrayList<>();
        try {
            while (rs.next()) {
                User user = new User()
                        .setId(rs.getInt(1)) //ID
                        .setFirstName(rs.getString(FIRST_NAME))
                        .setLastName(rs.getString(LAST_NAME))
                        .setRole(User.Role.values()[rs.getInt(ROLE)]);
                user.setAccount(new User.Account()
                        .setLogin(rs.getString(LOGIN))
                        .setPassword(rs.getString(PASSWORD))
                        .setToken(rs.getString(TOKEN)));
                if (user.getRole() == User.Role.STUDENT) {
                    PgGroupDao groupDao = new PgGroupDao(con);
                    user.setGroup(groupDao.getById(rs.getInt(GROUP_ID)));
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
            st.setInt(3, user.getRole().ordinal());
            if (user.getRole() == User.Role.STUDENT) {
                st.setInt(4, user.getGroup().getId());
            } else {
                st.setNull(4, java.sql.Types.NULL);
            }
            st.setString(5, user.getAccount().getLogin());
            st.setString(6, user.getAccount().getPassword());
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
            st.setString(5, user.getAccount().getLogin());
            st.setString(6, user.getAccount().getPassword());
            st.setString(7, user.getAccount().getToken());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public List<User> getByName(String name, User.Role role) {
        return getByParams(SQL_SELECT_USER_BY_NAME_AND_ROLE, "%" + name.toLowerCase() + "%", role.ordinal());
    }

    @Override
    public List<User> getByGroup(Integer groupId) {
        return getByParams(SQL_SELECT_USER_BY_GROUP, groupId);
    }

    @Override
    public List<User> getAll(User.Role role) {
        return getByParams(SQL_SELECT_USER_BY_ROLE, role.ordinal());
    }

    @Override
    public User getByToken(String token) {
        return getByParams(SQL_SELECT_USER_BY_TOKEN, token).get(0);
    }

    @Override
    public User getByLogin(String login, String password) {
        return getByParams(SQL_SELECT_USER_BY_LOGIN_AND_PASSWORD, login, password).get(0);
    }

}