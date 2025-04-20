package pl.cleankod.exchange.entrypoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cleankod.exchange.core.domain.Account;
import pl.cleankod.exchange.entrypoint.service.AccountLookupService;
import pl.cleankod.util.logging.LogTailUtil;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    private final AccountLookupService accountLookupService;

    public AccountController(AccountLookupService accountLookupService) {
        this.accountLookupService = accountLookupService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Account> findAccountById(@PathVariable String id, @RequestParam(required = false) String currency) {
        log.info("GET /accounts/{}/{}", LogTailUtil.maskLast(id), currency);

        return accountLookupService.findAccountByAccountId(id, currency)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/number={number}")
    public ResponseEntity<Account> findAccountByNumber(@PathVariable String number, @RequestParam(required = false) String currency) {
        log.info("GET /accounts/number={}, {}", LogTailUtil.maskLast(number), currency);

        return accountLookupService.findAccountByAccountNumber(number, currency)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
