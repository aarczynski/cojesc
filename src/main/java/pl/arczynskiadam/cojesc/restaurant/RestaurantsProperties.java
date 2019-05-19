package pl.arczynskiadam.cojesc.restaurant;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;

@ConfigurationProperties(prefix = "cojesc")
public class RestaurantsProperties {
    private HashMap<Restaurant, RestaurantProperties> restaurants;

    public RestaurantProperties getForRestaurant(Restaurant restaurant) {
        return restaurants.get(restaurant);
    }

    public HashMap<Restaurant, RestaurantProperties> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(HashMap<Restaurant, RestaurantProperties> restaurants) {
        this.restaurants = restaurants;
    }
}
