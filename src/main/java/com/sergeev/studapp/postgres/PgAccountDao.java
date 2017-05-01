package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.AccountDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.sergeev.studapp.model.Constants.*;

public class PgAccountDao extends PgGenericDao<Account> implements AccountDao {

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM accounts WHERE account_id = ?";
    }
    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM accounts";
    }
    @Override
    protected String getCreateQuery() {
        return "INSERT INTO accounts (login, password) VALUES (?, ?)";
    }
    @Override
    protected String getUpdateQuery() {
        return "UPDATE accounts SET login = ?, password = ?, token = ? WHERE account_id = ?";
    }
    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM accounts WHERE account_id = ?";
    }

    @Override
    protected List<Account> parseResultSet(ResultSet rs, Connection con) {
        List<Account> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Account account = new Account()
                        .setId(rs.getInt(ACCOUNT_ID))
                        .setLogin(rs.getString(LOGIN))
                        .setPassword(rs.getString(PASSWORD))
                        .setToken(rs.getString(TOKEN));
                list.add(account);
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
    protected void prepareStatementForInsert(PreparedStatement st, Account account) {
        try {
            st.setString(1, account.getLogin());
            st.setString(2, account.getPassword());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement st, Account account) {
        try {
            st.setString(1, account.getLogin());
            st.setString(2, account.getPassword());
            st.setString(3, account.getToken());
            st.setInt(4, account.getId());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

}
