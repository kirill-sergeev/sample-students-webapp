package com.sergeev.studapp;

import com.sergeev.studapp.dao.AccountDao;
import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Account;

public class Main {
      public static void main(String[] args) throws PersistentException {
          Account account = new Account();
          account.setLogin("kirill_sergeev");
          account.setPassword("123456");
          AccountDao accountDao = DaoFactory.getDaoFactory(DaoFactory.MONGO).getAccountDao();
          System.out.println(accountDao.save(account));
    }
}