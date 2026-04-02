package com.gateway.service;

import com.gateway.dao.AccountRepository;
import com.gateway.model.Account;

public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account createAccount(String name) {

        if (name == null || name.trim().isBlank()) {
            throw new RuntimeException("Invalid name");
        }

        Account account = new Account(name);

        repository.save(account);

        return account;
    }

    public Account getAccount(String id) {
        return repository.findById(id);
    }
}