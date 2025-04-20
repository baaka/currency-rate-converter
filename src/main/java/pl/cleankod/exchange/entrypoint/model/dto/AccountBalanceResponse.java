package pl.cleankod.exchange.entrypoint.model.dto;

import pl.cleankod.exchange.core.domain.Money;

import java.math.BigDecimal;
import java.util.Currency;

public record AccountBalanceResponse(BigDecimal amount, Currency currency) {
    public static AccountBalanceResponse of(Money balance) {
        return new AccountBalanceResponse(
                balance.amount(),
                balance.currency()
        );
    }
}
