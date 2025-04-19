package pl.cleankod.exchange.entrypoint.service;

import pl.cleankod.exchange.core.domain.Account;
import pl.cleankod.exchange.core.usecase.FindAccountAndConvertCurrencyUseCase;
import pl.cleankod.exchange.core.usecase.FindAccountUseCase;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Optional;

public class AccountLookupService {
    private final FindAccountAndConvertCurrencyUseCase findAccountAndConvertCurrencyUseCase;
    private final FindAccountUseCase findAccountUseCase;

    public AccountLookupService(FindAccountAndConvertCurrencyUseCase findAccountAndConvertCurrencyUseCase,
                                                    FindAccountUseCase findAccountUseCase) {
        this.findAccountAndConvertCurrencyUseCase = findAccountAndConvertCurrencyUseCase;
        this.findAccountUseCase = findAccountUseCase;
    }

    public Optional<Account> findAccountByAccountId(String accountId, String currencyCode) {
        Account.Id id = Account.Id.of(accountId);
        return currencyCode != null ? findAccountAndConvertCurrencyUseCase.execute(id, Currency.getInstance(currencyCode))
                : findAccountUseCase.execute(id);
    }

    public Optional<Account> findAccountByAccountNumber(String accountNumber, String currencyCode) {
        Account.Number number = Account.Number.of(URLDecoder.decode(accountNumber, StandardCharsets.UTF_8));
        return currencyCode != null ? findAccountAndConvertCurrencyUseCase.execute(number, Currency.getInstance(currencyCode))
                : findAccountUseCase.execute(number);
    }
}
