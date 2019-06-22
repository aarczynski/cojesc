package pl.arczynskiadam.cojesc.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.arczynskiadam.cojesc.restaurant.Restaurants
import pl.arczynskiadam.cojesc.service.MenuService
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest([LunchMenuController, Restaurants, DummyMenuService])
class LunchMenuControllerSpec extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private MenuService menuService

    def "should return lunch menu url"() {
        given:
        def testRestaurant = 'test-restaurant'
        menuService.findLunchMenu(_) >> Optional.of('<img src="https://some.image.url"/>')

        when:
        def result = mockMvc.perform(
                get("/menu/lunch/$testRestaurant")
                        .header("Accept", MediaType.TEXT_PLAIN)
        )

        then:
        result
                .andExpect(status().isOk())
                .andExpect(content().string('<img src="https://some.image.url"/>'))

    }

    @TestConfiguration
    static class DummyMenuService {
        private final DetachedMockFactory factory = new DetachedMockFactory()

        @Bean
        MenuService menuImageFilterService() {
            return factory.Mock(MenuService)
        }
    }
}
