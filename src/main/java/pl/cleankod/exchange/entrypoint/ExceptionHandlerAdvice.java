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
            ExchangeRatesNbpClientException.class,
            IllegalArgumentException.class
    })
    protected ResponseEntity<ApiError> handleKnownExceptions(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ApiError> handleUnexpectedExceptions(RuntimeException ex) {
        log.error("Unhandled runtime exception: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new ApiError("An unexpected error occurred. Please try again later."));
    }
}
