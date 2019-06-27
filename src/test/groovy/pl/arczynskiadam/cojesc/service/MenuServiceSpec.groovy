package pl.arczynskiadam.cojesc.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import pl.arczynskiadam.cojesc.CoJescApplication
import pl.arczynskiadam.cojesc.restaurant.FacebookAlbumRestaurant
import spock.lang.Specification
import spock.lang.Subject
import spock.mock.DetachedMockFactory

@SpringBootTest(classes = [ CoJescApplication, MockMenuServicesConfig ])
class MenuServiceSpec extends Specification {

    private static final FacebookAlbumRestaurant TEST_RESTAURANT = new FacebookAlbumRestaurant(name: 'test restaurant')

    @Subject
    @Autowired
    private MenuService service

    @Autowired
    private FacebookAlbumMenuService mockService

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
            service.findLunchMenu(TEST_RESTAURANT)
        }

        then:
        1 * mockService.getLunchMenuImageLink(TEST_RESTAURANT) >> Optional.of('http://some-url.com')
    }

    def "should not cache nulls"() {
        when:
        5.times {
            service.findLunchMenu(TEST_RESTAURANT)
        }

        then:
        5 * mockService.getLunchMenuImageLink(TEST_RESTAURANT) >> Optional.empty()
    }

    @TestConfiguration
    static class MockMenuServicesConfig {
        private final DetachedMockFactory factory = new DetachedMockFactory()

        @Primary
        @Bean
        FacebookAlbumMenuService facebookAlbumMenuService() {
            return factory.Mock(FacebookAlbumMenuService)
        }

        @Primary
        @Bean
        FacebookFeedMenuService facebookFeedMenuService() {
            return factory.Mock(FacebookFeedMenuService)
        }
    }
}
