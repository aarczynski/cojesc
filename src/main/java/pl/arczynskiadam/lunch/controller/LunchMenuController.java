package pl.arczynskiadam.lunch.controller;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.arczynskiadam.lunch.http.client.facebook.FacebookApiClient;
import pl.arczynskiadam.lunch.http.client.facebook.converter.RestaurantEnumConverter;
import pl.arczynskiadam.lunch.http.client.facebook.dto.album.Album;
import pl.arczynskiadam.lunch.restaurant.Restaurant;
import pl.arczynskiadam.lunch.restaurant.RestaurantProperties;
import pl.arczynskiadam.lunch.restaurant.RestaurantsProperties;

@RestController
@RequestMapping("/menu/lunch")
public class LunchMenuController {

    private FacebookApiClient facebookClient;
    private RestaurantsProperties restaurantsProperties;

    public LunchMenuController(FacebookApiClient facebookClient, RestaurantsProperties restaurantsProperties) {
        this.facebookClient = facebookClient;
        this.restaurantsProperties = restaurantsProperties;
    }

    @GetMapping("/{restaurant}")
    public String lunchMenu(@PathVariable("restaurant") Restaurant restaurant) {
        RestaurantProperties restaurantProperties = restaurantsProperties.getForRestaurant(restaurant);
        Album album = facebookClient.getAlbum(restaurantProperties.getFacebookAlbumId());
        return album.toString();
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Restaurant.class, new RestaurantEnumConverter());
    }
}
