package com.sergeev.studapp.dao;

import com.sergeev.studapp.model.Account;

public interface AccountDao extends GenericDao<Account>{

    Account getByToken(String token) throws PersistentException;
}
