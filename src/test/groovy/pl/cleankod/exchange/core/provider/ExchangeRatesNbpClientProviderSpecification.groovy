package pl.cleankod.exchange.core.provider

import org.springframework.core.env.Environment
import pl.cleankod.exchange.provider.ExchangeRatesNbpClientProvider
import pl.cleankod.exchange.provider.nbp.ExchangeRatesNbpClient
import spock.lang.Specification

class ExchangeRatesNbpClientProviderSpecification extends Specification {

    def "should provide cached client when cache is enabled"() {
        given:
        def env = Mock(Environment)
        env.getRequiredProperty("provider.nbp-api.base-url") >> "https://mock-nbp.pl"
        env.getProperty("provider.nbp-api.exchange-rates.cache.enabled", Boolean, false) >> true
        env.getProperty("provider.nbp-api.exchange-rate.cache.size", Integer, 200) >> 150

        when:
        def provider = new ExchangeRatesNbpClientProvider(env)
        ExchangeRatesNbpClient client = provider.provide()

        then:
        noExceptionThrown()
        client != null
    }

    def "should provide non-cached client when cache is disabled"() {
        given:
        def env = Mock(Environment)
        env.getRequiredProperty("provider.nbp-api.base-url") >> "https://mock-nbp.pl"
        env.getProperty("provider.nbp-api.exchange-rates.cache.enabled", Boolean, false) >> false

        when:
        def provider = new ExchangeRatesNbpClientProvider(env)
        ExchangeRatesNbpClient client = provider.provide()

        then:
        noExceptionThrown()
        client != null
    }

    def "should use default cache size when cache size property is missing"() {
        given:
        def env = Mock(Environment)
        env.getRequiredProperty("provider.nbp-api.base-url") >> "https://mock-nbp.pl"
        env.getProperty("provider.nbp-api.exchange-rates.cache.enabled", Boolean, false) >> true
        env.getProperty("provider.nbp-api.exchange-rate.cache.size", Integer, 200) >> 200

        when:
        def provider = new ExchangeRatesNbpClientProvider(env)
        ExchangeRatesNbpClient client = provider.provide()

        then:
        noExceptionThrown()
        client != null
    }

}
