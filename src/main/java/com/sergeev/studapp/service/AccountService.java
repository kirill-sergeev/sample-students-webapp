package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.AccountDao;
import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Account;
import com.sergeev.studapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AccountService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);
    private static final AccountDao ACCOUNT_DAO = DaoFactory.getDaoFactory().getAccountDao();

    public static Account create(User user) throws ApplicationException {
        String password = generatePassword();
        String login = generateLogin(user.getFirstName(), user.getLastName());

        Account account = new Account();
        account.setLogin(login);
        account.setPassword(password);

        try {
            ACCOUNT_DAO.save(account);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot create account.", e);
        }
        return account;
    }

    public static Account read(Integer id) throws ApplicationException {
        Account account;
        try {
            account = ACCOUNT_DAO.getById(id);
        } catch (PersistentException e) {
            throw new ApplicationException("Account not found.", e);
        }

        return account;
    }

    public static List<Account> readAll() {
        List<Account> accounts;

        try {
            accounts = ACCOUNT_DAO.getAll();
        } catch (PersistentException e) {
            accounts = Collections.emptyList();
        }

        return accounts;
    }

    public static Account update(User user) throws ApplicationException {
        Account account = new Account();
        Integer accountId = UserService.read(user.getId()).getAccount().getId();
        account.setId(accountId);
        account.setLogin(generateLogin(user.getFirstName(), user.getLastName()));
        account.setPassword(AccountService.read(accountId).getPassword());
        if (user.getAccount() == null) {
            account.setToken(UserService.read(user.getId()).getAccount().getToken());
        } else{
            account.setToken(user.getAccount().getToken());
        }
        try {
            ACCOUNT_DAO.update(account);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot update account.", e);
        }

        return account;
    }

    public static void delete(Integer id) throws ApplicationException {
        try {
            ACCOUNT_DAO.remove(id);
        } catch (PersistentException e) {
            throw new ApplicationException("Cannot remove account, because account not found.", e);
        }
    }

    private static String generateLogin(String firstName, String lastName) {
        return (firstName + "_" + lastName).toLowerCase();
    }

    private static String generatePassword() {
        Random random = new Random();
        return String.valueOf(random.nextInt(899999) + 100000);
    }
}

