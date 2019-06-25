package pl.arczynskiadam.cojesc.service

import pl.arczynskiadam.cojesc.client.facebook.graphapi.FacebookApiClient
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.Album
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.Albums
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.Image
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.ImageGroup
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.Photos
import pl.arczynskiadam.cojesc.client.google.ocr.GoogleOcrClient
import pl.arczynskiadam.cojesc.restaurant.FacebookAlbumRestaurant
import spock.lang.Specification
import spock.lang.Subject

import java.time.ZoneId
import java.time.ZonedDateTime

class FacebookAlbumMenuServiceSpec extends Specification {

    private static final String FACEBOOK_ID = '123456789'
    private static final Albums ALBUMS = new Albums([
            data: [
                    new Album(id: 'album-1', name: 'album one'),
                    new Album(id: 'album-2', name: 'album two')
            ]
    ])

    private FacebookApiClient facebookClient
    private GoogleOcrClient ocrClient
    private FacebookAlbumRestaurant restaurant

    @Subject
    private FacebookAlbumMenuService service

    void setup() {
        facebookClient = Mock()
        ocrClient = Mock()
        service = new FacebookAlbumMenuService(facebookClient, ocrClient)

        restaurant = new FacebookAlbumRestaurant(
                facebookId: FACEBOOK_ID,
                menuDuration: 1
        )

        ocrClient.imageContainsKeywords(new URL('http://www.some-host.com/not-lunch-img-link.jpg'), _) >> false
        ocrClient.imageContainsKeywords(new URL('http://www.some-host.com/new-lunch-img-link-small.jpg'), _) >> true
        ocrClient.imageContainsKeywords(new URL('http://www.some-host.com/new-lunch-img-link-big.jpg'), _) >> true
        ocrClient.imageContainsKeywords(new URL('http://www.some-host.com/old-lunch-img-link-small.jpg'), _) >> true
        ocrClient.imageContainsKeywords(new URL('http://www.some-host.com/old-lunch-img-link-big.jpg'), _) >> true

        facebookClient.getAlbums(FACEBOOK_ID) >> ALBUMS
    }

    def "should return newest lunch img"() {
        given:
        facebookClient.getPhotos('album-1') >> new Photos(
                data: [ notLunchImageGroup(), outOfDateLunchImageGroup() ]
        )
        facebookClient.getPhotos('album-2') >> new Photos(
                data: [ upToDateLunchImageGroup() ]
        )

        when:
        def link = service.getLunchMenuImageLink(restaurant)

        then:
        link.get() == 'http://www.some-host.com/new-lunch-img-link-big.jpg'
    }

    def "should return empty optional when there is no up to date menu"() {
        given:
        facebookClient.getPhotos(_) >> new Photos(
                data: [ outOfDateLunchImageGroup() ]
        )

        when:
        def link = service.getLunchMenuImageLink(restaurant)

        then:
        link.isEmpty()
    }

    def "should return empty optional when there is no menu"() {
        given:
        facebookClient.getPhotos(_) >> new Photos(
                data: [ notLunchImageGroup() ]
        )

        when:
        def link = service.getLunchMenuImageLink(restaurant)

        then:
        link.isEmpty()
    }

    private static ImageGroup upToDateLunchImageGroup() {
        new ImageGroup(
                createdTime: ZonedDateTime.now(ZoneId.systemDefault()),
                images: [
                        new Image(width: 1, height: 1, source: 'http://www.some-host.com/new-lunch-img-link-small.jpg'),
                        new Image(width: 2, height: 2, source: 'http://www.some-host.com/new-lunch-img-link-big.jpg'),
                ]
        )
    }

    private static ImageGroup outOfDateLunchImageGroup() {
        new ImageGroup(
                createdTime: ZonedDateTime.now(ZoneId.systemDefault()).minusYears(1),
                images: [
                        new Image(width: 1, height: 1, source: 'http://www.some-host.com/old-lunch-img-link-small.jpg'),
                        new Image(width: 2, height: 2, source: 'http://www.some-host.com/old-lunch-img-link-big.jpg'),
                ]
        )
    }

    private static ImageGroup notLunchImageGroup() {
        new ImageGroup(
                createdTime: ZonedDateTime.now(ZoneId.systemDefault()),
                images: [
                        new Image(width: 1, height: 1, source: 'http://www.some-host.com/not-lunch-img-link.jpg'),
                        new Image(width: 2, height: 2, source: 'http://www.some-host.com/not-lunch-img-link.jpg'),
                ]
        )
    }
}
