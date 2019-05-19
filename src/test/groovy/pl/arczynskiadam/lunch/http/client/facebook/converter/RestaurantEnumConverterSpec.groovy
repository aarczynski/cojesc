package pl.arczynskiadam.lunch.http.client.facebook.converter

import pl.arczynskiadam.lunch.restaurant.Restaurant
import spock.lang.Specification
import spock.lang.Subject

class RestaurantEnumConverterSpec extends Specification {

    @Subject
    RestaurantEnumConverter converter = new RestaurantEnumConverter()

    def "should convert string to Restaurant Enum"() {
        given:
        def restaurantName = 'wroclawska'

        when:
        converter.setAsText(restaurantName)

        then:
        converter.getValue() == Restaurant.WROCLAWSKA
    }
}
