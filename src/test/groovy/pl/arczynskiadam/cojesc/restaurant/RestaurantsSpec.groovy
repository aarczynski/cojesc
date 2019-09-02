package pl.arczynskiadam.cojesc.restaurant

import spock.lang.Specification
import spock.lang.Subject

class RestaurantsSpec extends Specification {

    @Subject
    public static final RESTAURANTS = new Restaurants(
            restaurants: [
                    new Restaurant(id: 'restaurant1'),
                    new Restaurant(id: 'restaurant2'),
                    new Restaurant(id: 'restaurant3')
            ]
    )

    def "should find restaurant by name"() {
        when:
        def restaurant = RESTAURANTS.getById('restaurant1')

        then:
        restaurant.id == 'restaurant1'
    }

    def "should find all restaurants"() {
        when:
        def restaurants = RESTAURANTS.getAll()

        then:
        restaurants*.id == ['restaurant1', 'restaurant2', 'restaurant3']
    }
}
