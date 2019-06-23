package pl.arczynskiadam.cojesc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.arczynskiadam.cojesc.restaurant.Restaurants;
import pl.arczynskiadam.cojesc.service.MenuService;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/menu/lunch")
public class LunchMenuController {

    private MenuService menuService;
    private Restaurants restaurants;

    public LunchMenuController(MenuService menuService, Restaurants restaurants) {
        this.menuService = menuService;
        this.restaurants = restaurants;
    }

    @GetMapping("/{restaurant}")
    public ResponseEntity<String> lunchMenu(@PathVariable("restaurant") String restaurant) {
        return menuService
                .findLunchMenu(restaurants.getByName(restaurant))
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }
}
