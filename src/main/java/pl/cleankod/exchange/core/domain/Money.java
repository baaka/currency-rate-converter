package pl.cleankod.exchange.core.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.cleankod.exchange.core.gateway.CurrencyConversionService;
import pl.cleankod.util.Preconditions;

import java.math.BigDecimal;
import java.util.Currency;

public record Money(BigDecimal amount, Currency currency) {
    private static final Logger log = LoggerFactory.getLogger(Money.class);

    public static Money of(BigDecimal amount, Currency currency) {
        Preconditions.requireNonNull(amount);
        Preconditions.requireNonNull(currency);
        return new Money(amount, currency);
    }

    public static Money of(String amount, String currency) {
        Preconditions.requireNonNull(amount);
        Preconditions.requireNonNull(currency);
        return new Money(new BigDecimal(amount), Currency.getInstance(currency));
    }

    public Money convert(CurrencyConversionService currencyConverter, Currency targetCurrency) {
        log.info("Converting currency to {}", targetCurrency);
        return currencyConverter.convert(this, targetCurrency);
    }
}
