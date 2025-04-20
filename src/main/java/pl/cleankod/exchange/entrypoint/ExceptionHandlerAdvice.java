package pl.cleankod.exchange.entrypoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.cleankod.exchange.core.usecase.CurrencyConversionException;
import pl.cleankod.exchange.entrypoint.model.ApiError;
import pl.cleankod.exchange.provider.nbp.exception.ExchangeRatesNbpClientException;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler({
            CurrencyConversionException.class,
            IllegalArgumentException.class,
            ExchangeRatesNbpClientException.class
    })
    protected ResponseEntity<ApiError> handleBadRequest(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new ApiError(ex.getMessage()));
    }
}
