package pl.arczynskiadam.cojesc.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.arczynskiadam.cojesc.restaurant.Restaurants
import pl.arczynskiadam.cojesc.service.MenuImageFilterService
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest(classes = [LunchMenuController, Restaurants, DummyMenuImageFilterService])
class LunchMenuControllerSpec extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private MenuImageFilterService menuImageFilterService

    def "should return lunch menu url"() {
        given:
        def testRestaurant = 'test-restaurant'
        menuImageFilterService.getLunchMenuImageLink(_) >> Optional.of('https://some.image.url')

        when:
        def result = mockMvc.perform(
                get("/menu/lunch/$testRestaurant")
                        .header("Accept", MediaType.TEXT_PLAIN)
        )

        then:
        result
                .andExpect(status().isOk())
                .andExpect(content().string('https://some.image.url'))

    }

    @TestConfiguration
    static class DummyMenuImageFilterService {
        private final DetachedMockFactory factory = new DetachedMockFactory()

        @Bean
        MenuImageFilterService menuImageFilterService() {
            return factory.Mock(MenuImageFilterService)
        }
    }
}
