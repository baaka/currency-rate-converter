package pl.cleankod.exchange.entrypoint.service

import pl.cleankod.exchange.core.domain.Account
import pl.cleankod.exchange.core.usecase.FindAccountAndConvertCurrencyUseCase
import pl.cleankod.exchange.core.usecase.FindAccountUseCase
import spock.lang.Specification

class AccountLookupServiceSpecification extends Specification {
    def findAccountAndConvertCurrencyUseCase = Mock(FindAccountAndConvertCurrencyUseCase)
    def findAccountUseCase = Mock(FindAccountUseCase)
    def accountLookupService = new AccountLookupService(findAccountAndConvertCurrencyUseCase, findAccountUseCase)

    def "test account lookup by ID with currency"() {
        given:
        String accountId = "fa07c538-8ce4-11ec-9ad5-4f5a625cd744"
        String currencyCode = "EUR"
        def accountIdObject = Account.Id.of(UUID.randomUUID())
        def accountNumberObject = Account.Number.of("65 1090 1665 0000 0001 0373 7343")
        def mockAccount = new Account(accountIdObject, accountNumberObject, null)

        when:
        findAccountAndConvertCurrencyUseCase.execute(_, _) >> Optional.of(mockAccount)
        def result = accountLookupService.findAccountByAccountId(accountId, currencyCode)

        then:
        result.isPresent()
        result.get() == mockAccount
    }

    def "test account lookup by ID without currency"() {
        given:
        String accountId = "fa07c538-8ce4-11ec-9ad5-4f5a625cd744"
        String currencyCode = null
        def accountIdObject = Account.Id.of(UUID.randomUUID())
        def accountNumberObject = Account.Number.of("65 1090 1665 0000 0001 0373 7343")
        def mockAccount = new Account(accountIdObject, accountNumberObject, null)

        when:
        findAccountUseCase.execute(_) >> Optional.of(mockAccount)
        def result = accountLookupService.findAccountByAccountId(accountId, currencyCode)

        then:
        result.isPresent()
        result.get() == mockAccount
    }
}
