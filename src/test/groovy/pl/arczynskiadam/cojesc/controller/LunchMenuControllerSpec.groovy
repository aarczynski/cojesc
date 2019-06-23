package pl.arczynskiadam.cojesc.controller

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.arczynskiadam.cojesc.restaurant.FacebookAlbumRestaurant
import pl.arczynskiadam.cojesc.restaurant.Restaurants
import pl.arczynskiadam.cojesc.service.MenuService
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [ LunchMenuController ])
class LunchMenuControllerSpec extends Specification {

    @Autowired
    private MockMvc mockMvc

    @SpringBean
    private MenuService menuService = Mock()

    @SpringBean
    private Restaurants restaurants = Mock()

    void setup() {
        restaurants.getByName(_) >> new FacebookAlbumRestaurant()
        restaurants.getAll() >> [
                new FacebookAlbumRestaurant(name: 'test-restaurant-1'),
                new FacebookAlbumRestaurant(name: 'test-restaurant-2')
        ]
    }

    def "should return lunch menu url"() {
        given:
        def testRestaurant = 'test-restaurant'
        menuService.findLunchMenu(_) >> Optional.of('<img src="https://some.image.url"/>')

        when:
        def result = mockMvc.perform(
                get("/restaurants/$testRestaurant/lunch")
                        .header("Accept", MediaType.TEXT_PLAIN)
        )

        then:
        result
                .andExpect(status().isOk())
                .andExpect(content().string('<img src="https://some.image.url"/>'))
    }

    def "should return 404 when lunch menu not found"() {
        given:
        def testRestaurant = 'test-restaurant'
        menuService.findLunchMenu(_) >> Optional.empty()

        when:
        def result = mockMvc.perform(
                get("/restaurants/$testRestaurant/lunch")
                        .header("Accept", MediaType.TEXT_PLAIN)
        )

        then:
        result
                .andExpect(status().isNotFound())
    }

    def "should return all restaurant names"() {
        when:
        def result = mockMvc.perform(
                get("/restaurants")
        )

        then:
        result
                .andExpect(status().isOk())
                .andExpect(content().string('["test-restaurant-1","test-restaurant-2"]'))
    }

    @TestConfiguration
    static class MockMenuService {
        private final DetachedMockFactory factory = new DetachedMockFactory()

        @Bean
        @Primary
        MenuService menuService() {
            return factory.Mock(MenuService)
        }
    }
}
