package pl.arczynskiadam.cojesc.service

import pl.arczynskiadam.cojesc.client.facebook.graphapi.FacebookApiClient
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.feed.Feed
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.feed.Post
import pl.arczynskiadam.cojesc.restaurant.FacebookFeedRestaurant
import spock.lang.Specification
import spock.lang.Subject

import java.time.ZoneId
import java.time.ZonedDateTime

class FacebookFeedMenuServiceSpec extends Specification {

    public static final String FACEBOOK_RESTAURANT_ID = 'test-restaurant'

    private FacebookApiClient facebookClient
    private FacebookFeedRestaurant restaurant

    @Subject
    private FacebookFeedMenuService service

    void setup() {
        facebookClient = Mock()
        service = new FacebookFeedMenuService(facebookClient)

        restaurant = new FacebookFeedRestaurant(
                facebookId: FACEBOOK_RESTAURANT_ID,
                menuKeyWords: ['lunch', 'menu'],
                menuDuration: 1
        )
    }

    def "should return newest lunch permalink"() {
        given:
        facebookClient.getPosts(FACEBOOK_RESTAURANT_ID) >> new Feed(
                data: [
                        notLunchPost(),
                        upToDateLunchPost(),
                        outOfDateLunchPost()
                ]
        )

        when:
        def link = service.getLunchMenuLink(restaurant)

        then:
        link.get() == 'http://new-lunch-permalink'
    }

    def "should return empty optional when there is no up to date menu"() {
        given:
        facebookClient.getPosts(FACEBOOK_RESTAURANT_ID) >> new Feed(
                data: [
                        notLunchPost(),
                        outOfDateLunchPost()
                ]
        )

        when:
        def link = service.getLunchMenuLink(restaurant)

        then:
        link.isEmpty()
    }

    def "should return empty optional when there is no menu"() {
        given:
        facebookClient.getPosts(FACEBOOK_RESTAURANT_ID) >> new Feed(
                data: [
                        notLunchPost()
                ]
        )

        when:
        def link = service.getLunchMenuLink(restaurant)

        then:
        link.isEmpty()
    }

    private static Post upToDateLunchPost() {
        new Post(message: 'this is lunch menu', permalink: 'http://new-lunch-permalink', createdTime: ZonedDateTime.now(ZoneId.systemDefault()))
    }

    private static Post outOfDateLunchPost() {
        new Post(message: 'this is lunch menu', permalink: 'http://old-lunch-permalink', createdTime: ZonedDateTime.now(ZoneId.systemDefault()).minusYears(1))
    }

    private static Post notLunchPost() {
        new Post(message: 'some message', permalink: 'http://some-permalink', createdTime: ZonedDateTime.now(ZoneId.systemDefault()))
    }
}
