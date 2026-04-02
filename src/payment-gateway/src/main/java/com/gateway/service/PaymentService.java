package com.gateway.service;

import com.gateway.config.DatabaseConfig;
import com.gateway.dao.AccountRepository;
import com.gateway.dao.LedgerRepository;
import com.gateway.dao.TransactionRepository;
import com.gateway.model.*;

import java.sql.Connection;
import java.util.UUID;

public class PaymentService {

    private final AccountRepository accountRepo;
    private final TransactionRepository txRepo;
    private final LedgerRepository ledgerRepo;

    public PaymentService(AccountRepository accountRepo,
                          TransactionRepository txRepo,
                          LedgerRepository ledgerRepo) {

        this.accountRepo = accountRepo;
        this.txRepo = txRepo;
        this.ledgerRepo = ledgerRepo;
    }

    public Transaction processPayment(String accountId,
                                      double amount,
                                      String idempotencyKey) {

        Transaction existing =
                txRepo.findByIdempotencyKey(idempotencyKey);

        if (existing != null) return existing;

        Transaction tx =
                new Transaction(
                        accountId,
                        amount,
                        idempotencyKey,
                        TransactionType.PAYMENT
                );

        try (Connection con = DatabaseConfig.getConnection()) {

            con.setAutoCommit(false);

            Account account =
                    accountRepo.findForUpdate(con, accountId);

            if (account == null) {
                tx.markFailed();
                txRepo.save(con, tx);
                con.commit();
                return tx;
            }

            double balance =
                    ledgerRepo.calculateBalance(con, accountId);

            if (balance < amount) {
                tx.markFailed();
                txRepo.save(con, tx);
                con.commit();
                return tx;
            }

            LedgerEntry entry =
                    new LedgerEntry(
                            accountId,
                            amount,
                            LedgerDirection.DEBIT,
                            tx.getId()
                    );

            ledgerRepo.save(con, entry);

            tx.markSuccess();
            txRepo.save(con, tx);

            con.commit();

            return tx;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Transaction deposit(String accountId, double amount) {

        Transaction tx =
                new Transaction(
                        accountId,
                        amount,
                        UUID.randomUUID().toString(),
                        TransactionType.DEPOSIT
                );

        try (Connection con = DatabaseConfig.getConnection()) {

            con.setAutoCommit(false);

            Account account =
                    accountRepo.findForUpdate(con, accountId);

            if (account == null) {
                tx.markFailed();
                txRepo.save(con, tx);
                con.commit();
                return tx;
            }

            LedgerEntry entry =
                    new LedgerEntry(
                            accountId,
                            amount,
                            LedgerDirection.CREDIT,
                            tx.getId()
                    );

            ledgerRepo.save(con, entry);

            tx.markSuccess();
            txRepo.save(con, tx);

            con.commit();

            return tx;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double getAccountBalance(String accountId) {

        try (Connection con = DatabaseConfig.getConnection()) {

            return ledgerRepo.calculateBalance(con, accountId);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}