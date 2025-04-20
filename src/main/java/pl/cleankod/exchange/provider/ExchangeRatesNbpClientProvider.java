package pl.cleankod.exchange.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import pl.cleankod.exchange.provider.nbp.ExchangeRatesNbpClient;
import pl.cleankod.exchange.provider.nbp.ExchangeRatesNbpClientFactory;

public class ExchangeRatesNbpClientProvider {
    private static final Logger log = LoggerFactory.getLogger(ExchangeRatesNbpClientProvider.class);
    private final Environment env;

    public ExchangeRatesNbpClientProvider(Environment env) {
        this.env = env;
    }

    public ExchangeRatesNbpClient provide() {
        String baseUrl = getBaseUrl();
        boolean cacheEnabled = isCacheEnabled();

        log.info("Using provider nbp-api.base-url: {}", baseUrl);
        log.info("Cache enabled for NBP client: {}", cacheEnabled);

        if (cacheEnabled) {
            int cacheSize = getCacheSize();
            log.info("Providing cached NBP Client [cache size: {}]", cacheSize);
            return ExchangeRatesNbpClientFactory.createCached(baseUrl, cacheSize);
        }

        log.info("Providing non-caching NBP Client");
        return ExchangeRatesNbpClientFactory.create(baseUrl);
    }

    private int getCacheSize() {
        return env.getProperty("provider.nbp-api.exchange-rate.cache.size", Integer.class, 200);
    }

    private String getBaseUrl() {
        return env.getRequiredProperty("provider.nbp-api.base-url");
    }

    private boolean isCacheEnabled() {
        return env.getProperty(
                "provider.nbp-api.exchange-rates.cache.enabled", Boolean.class, false);
    }
}
