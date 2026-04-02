package com.gateway.server;

import com.gateway.controller.AccountController;
import com.gateway.controller.DepositController;
import com.gateway.controller.PaymentController;
import com.gateway.dao.AccountRepository;
import com.gateway.dao.LedgerRepository;
import com.gateway.dao.TransactionRepository;
import com.gateway.service.AccountService;
import com.gateway.service.PaymentService;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class HttpServerApp {

    public static void main(String[] args) throws IOException {

        HttpServer server =
                HttpServer.create(new InetSocketAddress(8080), 0);

        server.setExecutor(Executors.newFixedThreadPool(20));

        AccountRepository accountRepo = new AccountRepository();
        TransactionRepository txRepo = new TransactionRepository();
        LedgerRepository ledgerRepo = new LedgerRepository();

        AccountService accountService =
                new AccountService(accountRepo);

        PaymentService paymentService =
                new PaymentService(accountRepo, txRepo, ledgerRepo);

        server.createContext("/accounts",
                new AccountController(accountService));

        server.createContext("/accounts/deposit",
                new DepositController(paymentService));

        server.createContext("/payments",
                new PaymentController(paymentService));

        server.start();

        System.out.println("Payment Gateway Server Running on 8080");
    }
}