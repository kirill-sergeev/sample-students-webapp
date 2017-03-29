package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.AccountDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PgAccountDao extends PgGenericDao<Account> implements AccountDao {

    private static final Logger LOG = LoggerFactory.getLogger(PgAccountDao.class);
    protected static final String ACCOUNT_ID = "account_id";
    protected static final String LOGIN = "login";
    protected static final String PASSWORD = "password";
    protected static final String TOKEN = "token";

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM accounts WHERE account_id= ?;";
    }
    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM accounts";
    }
    @Override
    protected String getCreateQuery() {
        return "INSERT INTO accounts (login, password) VALUES (?, ?);";
    }
    @Override
    protected String getUpdateQuery() {
        return "UPDATE accounts SET login= ?, password= ?, token= ? WHERE account_id= ?;";
    }
    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM accounts WHERE account_id= ?;";
    }

    @Override
    protected List<Account> parseResultSet(ResultSet rs) throws PersistentException {
        List<Account> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Account account = new Account();
                account.setId(rs.getString(ACCOUNT_ID));
                account.setLogin(rs.getString(LOGIN));
                account.setPassword(rs.getString(PASSWORD));
                account.setToken(rs.getString(TOKEN));
                result.add(account);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Account object) throws PersistentException {
        try {
            statement.setString(1, object.getLogin());
            statement.setString(2, object.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Account object) throws PersistentException {
        try {
            statement.setString(1, object.getLogin());
            statement.setString(2, object.getPassword());
            statement.setString(3, object.getToken());
            statement.setInt(4, Integer.parseInt(object.getId()));
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }
}
