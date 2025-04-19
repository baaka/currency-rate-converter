package pl.cleankod.util.cache


import pl.cleankod.util.cache.inmemory.CacheInMemoryLru
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

class CacheInMemoryClientSpecification extends Specification {
    def "should fetch and cache a value"() {
        given:
        def fetchCounter = new AtomicInteger()
        def cache = new CacheInMemoryClient<String, String>(
                { key ->
                    fetchCounter.incrementAndGet()
                    return "value-for-${key}"
                },
                new CacheInMemoryLru<>(10)
        )

        when:
        def first = cache.fetch("abc")
        def second = cache.fetch("abc")

        then:
        first == "value-for-abc"
        second == "value-for-abc"
        first.is(second)
        fetchCounter.get() == 1
    }

    def "should evict least recently used entry when cache size exceeded"() {
        given:
        def cache = new CacheInMemoryClient<Integer, Integer>(
                { it * 10 },
                new CacheInMemoryLru<>(3)
        )

        when:
        def r1 = cache.fetch(1)
        def r2 = cache.fetch(2)
        def r3 = cache.fetch(3)
        cache.fetch(1) // make it recently used
        def r4 = cache.fetch(4) // should evict 2

        then:
        r1 == 10
        r2 == 20
        r3 == 30
        r4 == 40

        and:
        cache.fetch(1) == 10
        cache.fetch(3) == 30
        cache.fetch(4) == 40

        and:
        cache.fetch(2) == 20
    }
}

