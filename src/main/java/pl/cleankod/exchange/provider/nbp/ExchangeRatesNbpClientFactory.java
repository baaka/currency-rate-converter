package pl.cleankod.exchange.provider.nbp;

import feign.Feign;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import pl.cleankod.exchange.provider.nbp.decoder.ExchangeRatesNbpClienErrorDecoder;
import pl.cleankod.exchange.provider.nbp.model.RateTableAndCurrencyCacheKey;
import pl.cleankod.exchange.provider.nbp.model.RateWrapper;
import pl.cleankod.util.cache.CacheInMemoryClient;
import pl.cleankod.util.cache.inmemory.CacheInMemoryLru;

public class ExchangeRatesNbpClientFactory {
    public static ExchangeRatesNbpClient create(String baseUrl) {
        return Feign.builder()
                .client(new ApacheHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new ExchangeRatesNbpClienErrorDecoder())
                .target(ExchangeRatesNbpClient.class, baseUrl);
    }

    public static ExchangeRatesNbpClient createCached(String baseUrl, int cacheSize) {
        ExchangeRatesNbpClient client = create(baseUrl);

        CacheInMemoryClient<RateTableAndCurrencyCacheKey, RateWrapper> cache = new CacheInMemoryClient<>(
                key -> client.fetch(key.table(), key.currency()),
                new CacheInMemoryLru<>(cacheSize)
        );

        return (table, currency) -> cache.fetch(new RateTableAndCurrencyCacheKey(table, currency));
    }
}
