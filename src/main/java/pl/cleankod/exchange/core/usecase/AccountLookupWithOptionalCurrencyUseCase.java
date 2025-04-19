package pl.cleankod.exchange.core.usecase;

import pl.cleankod.exchange.core.domain.Account;
import pl.cleankod.exchange.core.service.AccountLookupService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Optional;

public class AccountLookupWithOptionalCurrencyUseCase implements AccountLookupService {
    private final FindAccountAndConvertCurrencyUseCase findAccountAndConvertCurrencyUseCase;
    private final FindAccountUseCase findAccountUseCase;

    public AccountLookupWithOptionalCurrencyUseCase(FindAccountAndConvertCurrencyUseCase findAccountAndConvertCurrencyUseCase,
                                                    FindAccountUseCase findAccountUseCase) {
        this.findAccountAndConvertCurrencyUseCase = findAccountAndConvertCurrencyUseCase;
        this.findAccountUseCase = findAccountUseCase;
    }

    @Override
    public Optional<Account> findAccountByAccountId(String accountId, String currencyCode) {
        Account.Id id = Account.Id.of(accountId);
        return currencyCode != null ? findAccountAndConvertCurrencyUseCase.execute(id, Currency.getInstance(currencyCode))
                : findAccountUseCase.execute(id);
    }

    @Override
    public Optional<Account> findAccountByAccountNumber(String accountNumber, String currencyCode) {
        Account.Number number = Account.Number.of(URLDecoder.decode(accountNumber, StandardCharsets.UTF_8));
        return currencyCode != null ? findAccountAndConvertCurrencyUseCase.execute(number, Currency.getInstance(currencyCode))
                : findAccountUseCase.execute(number);
    }
}
