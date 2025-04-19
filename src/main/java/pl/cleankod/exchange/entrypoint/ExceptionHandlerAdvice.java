package pl.cleankod.exchange.entrypoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.cleankod.exchange.core.exception.ProviderClientException;
import pl.cleankod.exchange.core.usecase.CurrencyConversionException;
import pl.cleankod.exchange.entrypoint.model.ApiError;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler({
            CurrencyConversionException.class,
            IllegalArgumentException.class,
            ProviderClientException.class
    })
    protected ResponseEntity<ApiError> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new ApiError(ex.getMessage()));
    }
}
