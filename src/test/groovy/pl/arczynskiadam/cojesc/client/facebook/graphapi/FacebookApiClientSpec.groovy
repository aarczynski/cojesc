package pl.arczynskiadam.cojesc.client.facebook.graphapi

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.arczynskiadam.cojesc.CoJescApplication
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT

@SpringBootTest(classes = CoJescApplication)
class FacebookApiClientSpec extends Specification {

    @Subject
    @Autowired
    FacebookApiClient facebookClient

    @Shared
    WireMockServer wireMockServer

    void setup() {
        wireMockServer.stubFor(WireMock.any(WireMock.anyUrl()).willReturn(WireMock.aResponse().withStatus(I_AM_A_TEAPOT.value())))
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

    def "should perform http request and unmarshal response"() {
        given:
        wireMockServer.stubFor(
                WireMock.get(WireMock.urlMatching('/v3.3/[0-9]+/photos\\?.*'))
                        .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(facebookAlbumResponseJson())
                )
        )

        when:
        def album = facebookClient.getAlbum('111')

        then:
        album.data*.images*.source.flatten() == [
                'https://some.url/img1.jpg',
                'https://some.url/img2.jpg',
                'https://some.url/img3.jpg'
        ]
    }

    private static String facebookAlbumResponseJson() {
        """\
        {
            "data": [
                {
                    "images": [
                        {
                            "width": 1920,
                            "height": 1080,
                            "source": "https://some.url/img1.jpg"
                        },
                        {
                            "width": 1920,
                            "height": 1080,
                            "source": "https://some.url/img2.jpg"
                        }
                    ],
                    "created_time": "2019-05-28T18:00:00+0000",
                    "id": "1122334455667788"
                },
                {
                    "images": [
                        {
                            "width": 1920,
                            "height": 1080,
                            "source": "https://some.url/img3.jpg"
                        }
                    ],
                    "created_time": "2019-05-29T18:00:00+0000",
                    "id": "1111222233334444"
                }
            ]
        }
        """.stripIndent()
    }
}
