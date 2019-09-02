package pl.arczynskiadam.cojesc.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import pl.arczynskiadam.cojesc.CoJescApplication
import pl.arczynskiadam.cojesc.restaurant.Restaurant
import spock.lang.Specification
import spock.lang.Subject
import spock.mock.DetachedMockFactory

@SpringBootTest(classes = [ CoJescApplication, MockServicesConfig])
class MenuServiceSpec extends Specification {

    private static final Restaurant TEST_RESTAURANT = new Restaurant(id: 'test restaurant')

    @Subject
    @Autowired
    private MenuService menuService

    @Autowired
    private MenuRetrieveService mockMenuRetrieveService

    @Autowired
    private CacheManager cacheManager

    void cleanup() {
        cacheManager.getCacheNames().each {
            cacheManager.getCache(it).clear()
        }
    }

    def "should use cache"() {
        when:
        5.times {
            menuService.findLunchMenu(TEST_RESTAURANT)
        }

        then:
        1 * mockMenuRetrieveService.findLunchMenu(TEST_RESTAURANT) >> Optional.of('http://some-url.com')
    }

    def "should not cache nulls"() {
        when:
        5.times {
            menuService.findLunchMenu(TEST_RESTAURANT)
        }

        then:
        5 * mockMenuRetrieveService.findLunchMenu(TEST_RESTAURANT) >> Optional.empty()
    }

    @TestConfiguration
    static class MockServicesConfig {
        private final DetachedMockFactory factory = new DetachedMockFactory()

        @Primary
        @Bean
        MenuRetrieveService menuRetrieveService() {
            return factory.Mock(MenuRetrieveService)
        }
    }
}
