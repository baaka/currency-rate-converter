package pl.cleankod.exchange.entrypoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cleankod.exchange.core.domain.Account;
import pl.cleankod.exchange.core.service.AccountLookupService;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountLookupService accountLookupService;

    public AccountController(AccountLookupService accountLookupService) {
        this.accountLookupService = accountLookupService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Account> findAccountById(@PathVariable String id, @RequestParam(required = false) String currency) {
        return accountLookupService.findAccountByAccountId(id, currency)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/number={number}")
    public ResponseEntity<Account> findAccountByNumber(@PathVariable String number, @RequestParam(required = false) String currency) {
        return accountLookupService.findAccountByAccountNumber(number, currency)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
