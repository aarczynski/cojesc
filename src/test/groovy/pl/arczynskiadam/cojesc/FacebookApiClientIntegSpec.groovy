package pl.arczynskiadam.cojesc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.arczynskiadam.cojesc.http.client.facebook.FacebookApiClient
import spock.lang.Specification

@SpringBootTest(classes = CoJescApplication)
class FacebookApiClientIntegSpec extends Specification {

    @Autowired
    private FacebookApiClient fbClient

    def "should get facebook album data"() {
        given:
        def facebookAlbumId = '902399719798664'

        when:
        def album = fbClient.getAlbum(facebookAlbumId)

        then:
        album.data.size() > 0
    }
}
