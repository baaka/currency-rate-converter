package pl.cleankod.exchange.provider.nbp.exception;

public class ExchangeRatesNbpClientException extends RuntimeException {
    public ExchangeRatesNbpClientException(String message) {
        super(String.format("Exchange Rates NBP Client : %s", message));
    }
}
