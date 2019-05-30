package pl.arczynskiadam.cojesc.controller;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.converter.RestaurantEnumConverter;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;
import pl.arczynskiadam.cojesc.service.MenuImageFilterService;

@RestController
@RequestMapping("/menu/lunch")
public class LunchMenuController {

    private MenuImageFilterService menuService;

    public LunchMenuController(MenuImageFilterService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/{restaurant}")
    public String lunchMenu(@PathVariable("restaurant") Restaurant restaurant) {
        return menuService
                .getLunchMenuImageLink(restaurant)
                .orElse("Nie znaleziono aktualnego menu");
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Restaurant.class, new RestaurantEnumConverter());
    }
}
