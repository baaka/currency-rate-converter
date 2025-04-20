package pl.cleankod;

import feign.Feign;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import pl.cleankod.exchange.core.gateway.AccountRepository;
import pl.cleankod.exchange.core.gateway.CurrencyConversionService;
import pl.cleankod.exchange.core.usecase.FindAccountAndConvertCurrencyUseCase;
import pl.cleankod.exchange.core.usecase.FindAccountUseCase;
import pl.cleankod.exchange.entrypoint.AccountController;
import pl.cleankod.exchange.entrypoint.ExceptionHandlerAdvice;
import pl.cleankod.exchange.entrypoint.service.AccountLookupService;
import pl.cleankod.exchange.provider.AccountInMemoryRepository;
import pl.cleankod.exchange.provider.CurrencyConversionNbpService;
import pl.cleankod.exchange.provider.nbp.ExchangeRatesNbpClient;
import pl.cleankod.exchange.provider.nbp.decoder.ExchangeRatesNbpClienErrorDecoder;
import pl.cleankod.exchange.provider.nbp.model.RateTableAndCurrencyCacheKey;
import pl.cleankod.exchange.provider.nbp.model.RateWrapper;
import pl.cleankod.util.cache.CacheInMemoryClient;
import pl.cleankod.util.cache.inmemory.CacheInMemoryLru;
import pl.cleankod.util.logging.TraceIdFilter;

import java.util.Currency;

@SpringBootConfiguration
@EnableAutoConfiguration
public class ApplicationInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationInitializer.class, args);
    }

    @Bean
    AccountRepository accountRepository() {
        return new AccountInMemoryRepository();
    }

    @Bean
    ExchangeRatesNbpClient exchangeRatesNbpClient(Environment environment) {
        String nbpApiBaseUrl = environment.getRequiredProperty("provider.nbp-api.base-url");
        ExchangeRatesNbpClient feignClient = Feign.builder()
                .client(new ApacheHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new ExchangeRatesNbpClienErrorDecoder())
                .target(ExchangeRatesNbpClient.class, nbpApiBaseUrl);

        boolean enableNpbExchangeRatesCache = environment.getProperty(
                "provider.nbp-api.exchange-rates.cache.enabled",
                Boolean.class,
                false);
        if (enableNpbExchangeRatesCache) {
            Integer nbpExchangeRateCacheMaxSize = environment.getProperty(
                    "provider.nbp-api.exchange-rate.cache.size",
                    Integer.class,
                    200);

            CacheInMemoryClient<RateTableAndCurrencyCacheKey, RateWrapper> cache = new CacheInMemoryClient<>(
                    key -> feignClient.fetch(key.table(), key.currency()),
                    new CacheInMemoryLru<>(nbpExchangeRateCacheMaxSize)
            );

            return (table, currency) -> cache.fetch(new RateTableAndCurrencyCacheKey(table, currency));
        }

        return feignClient;
    }

    @Bean
    CurrencyConversionService currencyConversionService(ExchangeRatesNbpClient exchangeRatesNbpClient) {
        return new CurrencyConversionNbpService(exchangeRatesNbpClient);
    }

    @Bean
    FindAccountUseCase findAccountUseCase(AccountRepository accountRepository) {
        return new FindAccountUseCase(accountRepository);
    }

    @Bean
    FindAccountAndConvertCurrencyUseCase findAccountAndConvertCurrencyUseCase(
            AccountRepository accountRepository,
            CurrencyConversionService currencyConversionService,
            Environment environment
    ) {
        Currency baseCurrency = Currency.getInstance(environment.getRequiredProperty("app.base-currency"));
        return new FindAccountAndConvertCurrencyUseCase(accountRepository, currencyConversionService, baseCurrency);
    }

    @Bean
    AccountController accountController(AccountLookupService accountLookupService) {
        return new AccountController(accountLookupService);
    }

    @Bean
    ExceptionHandlerAdvice exceptionHandlerAdvice() {
        return new ExceptionHandlerAdvice();
    }

    @Bean
    AccountLookupService accountLookupService(FindAccountAndConvertCurrencyUseCase findAccountAndConvertCurrencyUseCase,
                                              FindAccountUseCase findAccountUseCase) {
        return new AccountLookupService(findAccountAndConvertCurrencyUseCase, findAccountUseCase);
    }

    @Bean
    public FilterRegistrationBean<TraceIdFilter> traceIdFilter() {
        FilterRegistrationBean<TraceIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TraceIdFilter());
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
