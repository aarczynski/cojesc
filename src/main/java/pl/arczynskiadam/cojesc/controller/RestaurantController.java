package pl.arczynskiadam.cojesc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;
import pl.arczynskiadam.cojesc.restaurant.Restaurants;
import pl.arczynskiadam.cojesc.service.MenuService;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private MenuService menuService;
    private Restaurants restaurants;

    public RestaurantController(MenuService menuService, Restaurants restaurants) {
        this.menuService = menuService;
        this.restaurants = restaurants;
    }

    @GetMapping
    public List<String> supportedRestaurants() {
        return restaurants.getAll().stream()
                .map(Restaurant::getName)
                .collect(toList());
    }

    @GetMapping("/{restaurant}/lunch")
    public ResponseEntity<String> lunchMenu(@PathVariable("restaurant") String restaurant) {
        return menuService
                .findLunchMenu(restaurants.getByName(restaurant))
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }
}
