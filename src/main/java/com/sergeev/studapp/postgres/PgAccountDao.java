package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.AccountDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PgAccountDao extends PgGenericDao<Account> implements AccountDao {

    protected static final String ACCOUNT_ID = "account_id";
    protected static final String LOGIN = "login";
    protected static final String PASSWORD = "password";

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM accounts WHERE account_id= ?;";
    }

    public String getSelectAllQuery() {
        return "SELECT * FROM accounts";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO accounts (login, password) VALUES (?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE accounts SET login= ?, password= ? WHERE account_id= ?;";
    }

    @Override
    public String getDeleteQuery() {
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
                result.add(account);
            }
        } catch (Exception e) {
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
            statement.setInt(3, Integer.parseInt(object.getId()));
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

}
