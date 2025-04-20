package pl.cleankod.exchange.entrypoint.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.cleankod.exchange.core.domain.Account;
import pl.cleankod.exchange.core.usecase.FindAccountAndConvertCurrencyUseCase;
import pl.cleankod.exchange.core.usecase.FindAccountUseCase;
import pl.cleankod.util.logging.LogTailUtil;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Optional;

public class AccountLookupService {
    private static final Logger log = LoggerFactory.getLogger(AccountLookupService.class);
    private final FindAccountAndConvertCurrencyUseCase findAccountAndConvertCurrencyUseCase;
    private final FindAccountUseCase findAccountUseCase;

    public AccountLookupService(FindAccountAndConvertCurrencyUseCase findAccountAndConvertCurrencyUseCase,
                                FindAccountUseCase findAccountUseCase) {
        this.findAccountAndConvertCurrencyUseCase = findAccountAndConvertCurrencyUseCase;
        this.findAccountUseCase = findAccountUseCase;
    }

    public Optional<Account> findAccountByAccountId(String accountId, String currencyCode) {
        Account.Id id = Account.Id.of(accountId);

        log.info("Looking up account by account id {}, currency {}", id.value(), currencyCode);

        return currencyCode != null ? findAccountAndConvertCurrencyUseCase.execute(id, Currency.getInstance(currencyCode))
                : findAccountUseCase.execute(id);
    }

    public Optional<Account> findAccountByAccountNumber(String accountNumber, String currencyCode) {
        log.info("Looking up account by account number {}, currency {}", LogTailUtil.maskLast(accountNumber), currencyCode);

        Account.Number number = Account.Number.of(URLDecoder.decode(accountNumber, StandardCharsets.UTF_8));
        return currencyCode != null ? findAccountAndConvertCurrencyUseCase.execute(number, Currency.getInstance(currencyCode))
                : findAccountUseCase.execute(number);
    }
}
