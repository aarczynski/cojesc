package pl.arczynskiadam.cojesc.controller

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.arczynskiadam.cojesc.CoJescApplication
import spock.lang.Shared
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest(classes = CoJescApplication)
class LunchMenuControllerSpec extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Shared
    WireMockServer wireMockServer

    void setup() {
        wireMockServer.stubFor(
                WireMock.any(WireMock.anyUrl())
                        .willReturn(WireMock.aResponse()
                        .withStatus(I_AM_A_TEAPOT.value())
                )
        )
    }

    void cleanup() {
        wireMockServer.resetAll()
    }

    void setupSpec() {
        wireMockServer = new WireMockServer(wireMockConfig().port(8090))
        wireMockServer.start()
    }

    void cleanupSpec() {
        wireMockServer.stop()
        wireMockServer.shutdown()
    }

    def "should return facebook album data"() {
        given:
        def testRestaurant = 'wroclawska'

        wireMockServer.stubFor(
                WireMock.get(WireMock.urlMatching('/v3.3/[0-9]+/photos\\?.*'))
                        .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"data\":[]}")
                )
        )

        when:
        def result = mockMvc.perform(
                get("/menu/lunch/$testRestaurant")
                        .header("Accept", MediaType.TEXT_PLAIN)
        )

        then:
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.*').isEmpty())

    }
}
