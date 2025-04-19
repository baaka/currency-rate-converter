package pl.cleankod.exchange.provider.nbp.decoder;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import pl.cleankod.exchange.provider.nbp.exception.ExchangeRatesNbpClientException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ExchangeRatesNbpClienErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        String message = "Unexpected error from NBP API";

        try {
            if (response.body() != null) {
                message = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            message = "Failed to read error response from NBP API";
        }

        // source: http://api.nbp.pl/en.html#errors
        return switch (response.status()) {
            case 400 -> message != null && message.toLowerCase().contains("limit exceeded") ?
                    new ExchangeRatesNbpClientException(String.format("Data limit exceeded: %s", message))
                    : new ExchangeRatesNbpClientException(String.format("Bad request to NBP API: %s", message));
            case 404 ->
                    new ExchangeRatesNbpClientException(String.format("Data not found for given parameters: %s", message));
            default -> defaultDecoder.decode(methodKey, response);
        };
    }
}
