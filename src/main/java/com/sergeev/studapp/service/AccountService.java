package com.sergeev.studapp.service;

import com.sergeev.studapp.dao.AccountDao;
import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Account;
import com.sergeev.studapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AccountService {
    private static AccountDao accountDao = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getAccountDao();

    public static Account create(User user) {
        String password = generatePassword();
        String login = generateLogin(user.getFirstName(), user.getLastName());
        Account account = new Account();
        account.setLogin(login);
        account.setPassword(password);

        try {
            account = accountDao.persist(account);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return account;
    }

    public static Account read(String id) {
        Account account = null;

        try {
            account = accountDao.getById(id);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return account;
    }

    public static Account readByToken(String token) {
        Account account = null;

        try {
            account = accountDao.getByToken(token);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return account;
    }

    public static List<Account> readAll() {
        List<Account> accounts = new ArrayList<>();

        try {
            accounts = accountDao.getAll();
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
            accountDao.update(account);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        return account;
    }

    public static void delete(String id) {
        try {
            accountDao.delete(id);
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

