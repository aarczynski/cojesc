package pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album

import spock.lang.Specification

class ImageGroupSpec extends Specification {

    def "should return biggest image"() {
        given:
        def imageGroup = new ImageGroup(
                images: [
                        new Image(width: 1, height: 1),
                        new Image(width: 1, height: 2),
                        new Image(width: 2, height: 2),
                        new Image(width: 2, height: 1)
                ]
        )

        when:
        def biggest = imageGroup.findBiggestImage()

        then:
        biggest == imageGroup.images[2]
    }
}
