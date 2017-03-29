package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.AccountDao;
import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Account;
import com.sergeev.studapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AccountService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);
    private static final AccountDao ACCOUNT_DAO = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getAccountDao();

    public static Account create(User user) {
        String password = generatePassword();
        String login = generateLogin(user.getFirstName(), user.getLastName());
        Account account = new Account();
        account.setLogin(login);
        account.setPassword(password);

        try {
            account = ACCOUNT_DAO.persist(account);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return account;
    }

    public static Account read(String id) {
        Account account = null;

        try {
            account = ACCOUNT_DAO.getById(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return account;
    }

    public static List<Account> readAll() {
        List<Account> accounts = new ArrayList<>();

        try {
            accounts = ACCOUNT_DAO.getAll();
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    public static Account update(User user) {
        String accountId = UserService.read(user.getId()).getAccount().getId();
        Account account = new Account();
        account.setId(accountId);
        account.setLogin(generateLogin(user.getFirstName(), user.getLastName()));
        account.setPassword(AccountService.read(accountId).getPassword());
        account.setToken(user.getAccount().getToken());

        try {
            ACCOUNT_DAO.update(account);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return account;
    }

    public static void delete(String id) {
        try {
            ACCOUNT_DAO.delete(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }
    }

    private static String generateLogin(String firstName, String lastName){
        String login = firstName.toLowerCase() + "_" + lastName.toLowerCase();
        return login;
    }

    private static String generatePassword(){
        Random random = new Random();
        String password = String.valueOf(random.nextInt(899999) + 100000);
        return password;
    }
}

