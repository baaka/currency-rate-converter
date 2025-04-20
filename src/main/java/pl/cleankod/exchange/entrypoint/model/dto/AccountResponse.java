package pl.cleankod.exchange.entrypoint.model.dto;

import pl.cleankod.exchange.core.domain.Account;

import java.util.UUID;

public record AccountResponse(UUID id, String number, AccountBalanceResponse balance) {
    public static AccountResponse of(Account account) {
        return new AccountResponse(
                account.id().value(),
                account.number().value(),
                AccountBalanceResponse.of(account.balance())
        );
    }
}


