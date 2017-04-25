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

import static com.sergeev.studapp.postgres.PgConstants.*;

public class PgAccountDao extends PgGenericDao<Account> implements AccountDao {

    private static final Logger LOG = LoggerFactory.getLogger(PgAccountDao.class);

    @Override
    protected String getSelectQuery() {
        return String.format("SELECT * FROM %s WHERE %s= ?", ACCOUNTS, ACCOUNT_ID);
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
                account.setId(rs.getInt(ACCOUNT_ID));
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
            throw new PersistentException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Account object) throws PersistentException {
        try {
            statement.setString(1, object.getLogin());
            statement.setString(2, object.getPassword());
            statement.setString(3, object.getToken());
            statement.setInt(4, object.getId());
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }
}
