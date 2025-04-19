package pl.cleankod.exchange.core.service;

import pl.cleankod.exchange.core.domain.Account;

import java.util.Optional;

public interface AccountLookupService {
    Optional<Account> findAccountByAccountId(String accountId, String currencyCode);

    Optional<Account> findAccountByAccountNumber(String accountNumber, String currencyCode);
}
