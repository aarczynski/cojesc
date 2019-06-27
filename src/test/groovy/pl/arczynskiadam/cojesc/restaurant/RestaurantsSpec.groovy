package pl.arczynskiadam.cojesc.restaurant

import spock.lang.Specification
import spock.lang.Subject

class RestaurantsSpec extends Specification {

    @Subject
    public static final RESTAURANTS = new Restaurants(
            fbAlbumRestaurants: [
                    new FacebookAlbumRestaurant(name: 'restaurant1')
            ],
            fbFeedRestaurants: [
                    new FacebookFeedRestaurant(name: 'restaurant2')
            ],
            wwwRestaurants: [
                    new WwwRestaurant(name: 'restaurant3')
            ]
    )

    def "should find restaurant by name"() {
        when:
        def restaurant = RESTAURANTS.getByName('restaurant1')

        then:
        restaurant.name == 'restaurant1'
    }

    def "should find all restaurants"() {
        when:
        def restaurants = RESTAURANTS.getAll()

        then:
        restaurants*.name == ['restaurant1', 'restaurant2', 'restaurant3']
    }
}
